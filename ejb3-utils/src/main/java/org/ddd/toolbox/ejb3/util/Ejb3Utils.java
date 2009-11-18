package org.ddd.toolbox.ejb3.util;

import javax.ejb.SessionContext;

import org.apache.commons.lang.StringUtils;

/**
 * Ejb3Utils
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions:
 * jeremy.norris: Feb 22, 2007: Initial revision.
 *
 * @author jeremy.norris
 */
public class Ejb3Utils {

    private Ejb3Utils() {
    }

    /**
     * Create an association path with the provided properties.
     *
     * @param properties The properties to join.
     * @return The resulting association path.
     */
    public static String associationPath(String... properties) {
        return StringUtils.join(properties, ".");
    }

    /**
     * Create an association path with the provided properties and alias.
     *
     * @param alias
     *            The alias to use for the resulting association path.
     * @param properties
     *            The properties to join.
     * @return The resulting association path.
     */
    public static String associationPathWithAlias(String alias, String... properties) {
        return alias + "." + associationPath(properties);
    }

    /**
     * Create a query parameter name string.
     *
     * @param name
     *            The name of the parameter.
     * @return The parameter name with a ":" preceding it.
     */
    public static String parameterName(String name) {
        return ":" + name;
    }

    /**
     * Get a bean reference to the currently bean that is currently being
     * invoked.
     *
     * @param <T>
     *            The type of the business interface.
     * @param sessionContext
     *            The session context to lookup.
     * @return The bean reference.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCurrentBeanReference(SessionContext sessionContext) {
        return (T) sessionContext.getEJBObject();
    }

    /**
     * Construct an EJB (for out of container testing).
     *
     * Note: This function is a very expedient hack for quick testing. It has
     * many limitations including a requirement that the interface
     * implementation is "*Bean". It should be removed as soon as the EJB3
     * embeddable stuff is ready (20070718).
     *
     * @param beanClass
     *            The bean class to create.
     * @param em
     *            The entity manager to inject.
     * @return The newly constructed bean.
    public static Object createEJB(Class<?> beanClass, EntityManager em) {

        Object result = null;

        try {
            Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if (constructor.getParameterTypes().length == 0) {
                    constructor.setAccessible(true);
                    result = constructor.newInstance();
                    break;
                }
            }
        }
        catch (SecurityException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Inject entity manager (if needed):
        if (result instanceof GenericEjb3Hibernate3DAO) {
            Ejb3HibernateUtil.setEntityManager((GenericEjb3Hibernate3DAO<?, ?>) result, em, true);
        }

        // Inject EJB dependencies:
        try {
            for (Field field : beanClass.getDeclaredFields()) {
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType().equals(EJB.class)) {
                        Class<?> type = field.getType();
                        Class<?> implType = Class.forName(type.getCanonicalName() + "Bean");
                        Object inject = null;
                        // Deal with "local" injection:
                        if (!beanClass.equals(implType)) {
                            inject = createEJB(implType, em);
                        }
                        else {
                            inject = result;
                        }
                        field.setAccessible(true);
                        field.set(result, inject);
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
     */

}
