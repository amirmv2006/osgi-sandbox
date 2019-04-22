package ir.amv.snippets.my.enhancer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.ListenerHook;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;

import static org.osgi.framework.ServiceEvent.*;

/**
 * @author Amir
 */
public class MyEventHook implements EventListenerHook {
    public static final String PROXY = "AmirProxy";
    public static final String SHOULD_PROXY = "SHOULD_PROXY";
    private BundleContext bc;

    public MyEventHook(final BundleContext bc) {
        this.bc = bc;
    }

    @Override
    public void event(final ServiceEvent event, final Map<BundleContext, Collection<ListenerHook.ListenerInfo>> listeners) {
        final ServiceReference serviceReference = event.getServiceReference();
        System.out.println(serviceReference.getBundle().getSymbolicName());

        if (serviceReference.getProperty(PROXY) == null &&
                serviceReference.getProperty(SHOULD_PROXY) != null &&
                serviceReference.getBundle().getBundleContext() != bc) {
            Bundle bundle = serviceReference.getBundle();

            try {
                switch (event.getType()) {
                    case REGISTERED: {

                        String[] propertyKeys = serviceReference.getPropertyKeys();
                        Dictionary properties = buildProps(propertyKeys, event);
                        String[] interfaces = (String[]) serviceReference.getProperty(
                                "objectClass");

                        Class[] toClass = toClass(interfaces, bundle);
                        proxyService(bundle,
                                interfaces,
                                toClass,
                                properties,
                                this.getClass().getClassLoader(), new LoggerProxy(
                                        bc, serviceReference));



                        break;
                    }
                    case UNREGISTERING: {
                        //TODO
                        break;
                    }
                    case MODIFIED:
                    case MODIFIED_ENDMATCH: {
                        //TODO
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void proxyService(
            final Bundle bundle,
            final String[] interfaces, final Class[] toClass,
            final Dictionary properties,
            final ClassLoader classLoader,
            final LoggerProxy loggerProxy) {
        properties.put(PROXY, true);
        Object proxy = Proxy.newProxyInstance(toClass[0].getClassLoader(), toClass, loggerProxy);
        bundle.getBundleContext().registerService(interfaces, proxy, properties);
    }

    private Class[] toClass(final String[] interfaces, final Bundle bundle) throws ClassNotFoundException {
        Class[] result = new Class[interfaces.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = bundle.loadClass(interfaces[i]);
        }
        return result;
    }

    private Dictionary<Object, Object> buildProps(final String[] propertyKeys, final ServiceEvent event) {
        Properties result = new Properties();
        if (propertyKeys != null) {
            for (String propertyKey : propertyKeys) {
                result.put(propertyKey, event.getServiceReference().getProperty(propertyKey));
            }
        }
        return result;
    }
}
