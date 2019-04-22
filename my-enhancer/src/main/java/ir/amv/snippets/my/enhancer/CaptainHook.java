package ir.amv.snippets.my.enhancer;

import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;

/**
 * @author Amir
 */
public class CaptainHook implements WeavingHook {
    @Override
    public void weave(final WovenClass wovenClass) {
        Class<?> definedClass = wovenClass.getDefinedClass();
        System.out.println("definedClass = " + definedClass);
        String className = wovenClass.getClassName();
        System.out.println("className = " + className);
    }
}
