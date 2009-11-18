package org.ddd.toolbox.jpa.l10n;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * L10nValueInteger
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
@MappedSuperclass
public abstract class L10nValueInteger extends L10nValue<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * @frameworkUseOnly
     */
    protected L10nValueInteger() {
        super();
    }

    /**
     * @param locale
     *            The locale of this value.
     * @param value
     *            The value of the object.
     */
    protected L10nValueInteger(Locale locale, Integer value) {
        super(locale, value);
    }

    /**
     * @return The value of the object.
     */
    @Column(name = "value", nullable = false)
    public Integer getValue() {
        return super.getValue();
    }

    /**
     * @param value The value of the object.
     */
    public void setValue(Integer value) {
        super.setValue(value);
    }

}
