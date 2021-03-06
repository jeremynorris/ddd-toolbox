package org.ddd.toolbox.jpa.l10n;

/**
 * L10nBundleString
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions: jnorris: Sep 11, 2008: Initial revision.
 *
 * @param <T_PARENT>
 *            The type of the parent of this bundle.
 * @param <T_L10N_VALUE>
 *            The type of the l10n value wrapper.
 *
 * @author jnorris
 */
public abstract class L10nBundleString<T_PARENT, T_L10N_VALUE extends L10nValueString> extends L10nBundle<T_PARENT, String, T_L10N_VALUE> {

    private static final long serialVersionUID = 1L;

    /**
     * @frameworkUseOnly
     */
    protected L10nBundleString() {
        super();
    }

    /**
     * @param parent
     *            The parent of this bundle.
     */
    protected L10nBundleString(T_PARENT parent) {
        super(parent);
    }

}
