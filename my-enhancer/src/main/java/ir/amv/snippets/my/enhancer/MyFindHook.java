package ir.amv.snippets.my.enhancer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.service.FindHook;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Amir
 */
public class MyFindHook implements FindHook {
    private final BundleContext bc;

    public MyFindHook(final BundleContext bc) {
        this.bc = bc;
    }

    @Override
    public void find(final BundleContext context, final String name, final String filter, final boolean allServices, final Collection<ServiceReference<?>> references) {
        try {
            if (this.bc.equals(context) || context.getBundle().getBundleId() == 0) {
                return;
            }

            Iterator iterator = references.iterator();

            while (iterator.hasNext()) {
                ServiceReference sr = (ServiceReference) iterator.next();

                System.out.println(
                        "from bundle" + sr.getBundle().getSymbolicName());

                if (sr.getProperty(MyEventHook.SHOULD_PROXY) != null &&
                        sr.getProperty(MyEventHook.PROXY) == null) {
                    iterator.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
