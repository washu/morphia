/**
 *
 */
package com.github.jmkgreen.morphia.mapping.lazy;

import com.github.jmkgreen.morphia.logging.Logr;
import com.github.jmkgreen.morphia.logging.MorphiaLoggerFactory;

/**
 * @author Uwe Schaefer, (us@thomas-daily.de)
 */
public class LazyFeatureDependencies {

    private static final Logr logger = MorphiaLoggerFactory.get(LazyFeatureDependencies.class);
    private static Boolean fullFilled;

    private LazyFeatureDependencies() {
    }

    public static boolean assertDependencyFullFilled() {
        boolean fullfilled = testDependencyFullFilled();
        if (!fullfilled)
            logger.warning("Lazy loading impossible due to missing dependencies.");
        return fullfilled;
    }

    public static boolean testDependencyFullFilled() {
        if (fullFilled != null)
            return fullFilled;
        try {
            fullFilled = Class.forName("net.sf.cglib.proxy.Enhancer") != null
                    && Class.forName("com.thoughtworks.proxy.toys.hotswap.HotSwapping") != null;
        } catch (ClassNotFoundException e) {
            fullFilled = false;
        }
        return fullFilled;
    }

    /**
     * @return A LazyProxyFactory
     */
    public static LazyProxyFactory createDefaultProxyFactory() {
        if (testDependencyFullFilled()) {
            String factoryClassName = "com.github.jmkgreen.morphia.mapping.lazy.CGLibLazyProxyFactory";
            try {
                return (LazyProxyFactory) Class.forName(factoryClassName).newInstance();
            } catch (Exception e) {
                logger.error("While instanciating " + factoryClassName, e);
            }
        }
        return null;
    }
}
