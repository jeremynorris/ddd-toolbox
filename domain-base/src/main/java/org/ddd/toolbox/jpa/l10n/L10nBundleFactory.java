package org.ddd.toolbox.jpa.l10n;

/**
 * L10nBundleFactory
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions:
 * jnorris: Sep 11, 2008: Initial revision.
 *
 * @author jnorris
 */
public class L10nBundleFactory {

    /**
     * Private constructor.
     */
    private L10nBundleFactory() {
    }

    /**
     * Create a new l10n bundle.
     *
     * @param clazz The bundle class to instantiate.
     *
     * @param <T_L10N_BUNDLE>
     *            The type of the 1l0n bundle.
     * @return The new l10n bundle.
     */
    public static <T_L10N_BUNDLE> T_L10N_BUNDLE createBundle2(Class<T_L10N_BUNDLE> clazz) {

        T_L10N_BUNDLE result = null;
        try {
            result = clazz.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Create a new l10n bundle entry.
     *
     * @param clazz
     *            The bundle entry class to instantiate.
     *
     * @param <T_L10N_VALUE>
     *            The type of the l10n value.
     * @return The new l10n bundle entry.
     */
    public static <T_L10N_VALUE> T_L10N_VALUE createBundleEntry(Class<T_L10N_VALUE> clazz) {

        T_L10N_VALUE result = null;
        try {
            result = clazz.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
