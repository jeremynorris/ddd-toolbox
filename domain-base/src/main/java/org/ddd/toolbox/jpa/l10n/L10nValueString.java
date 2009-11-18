package org.ddd.toolbox.jpa.l10n;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * L10nValueString
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
public abstract class L10nValueString extends L10nValue<String> {

    private static final long serialVersionUID = 1L;

    /**
     * @frameworkUseOnly
     */
    protected L10nValueString() {
        super();
    }

    /**
     * @param locale
     *            The locale of this value.
     * @param value
     *            The value of the object.
     */
    protected L10nValueString(Locale locale, String value) {
        super(locale, value);
    }

    /**
     * @return The value of the object.
     */
    @Column(name = "value", nullable = false)
    public String getValue() {
        return super.getValue();
    }

    /**
     * @param value The value of the object.
     */
    public void setValue(String value) {
        super.setValue(value);
    }

}
