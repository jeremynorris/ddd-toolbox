package org.ddd.toolbox.jpa.l10n;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.ddd.toolbox.jpa.TrackedEntity;
import org.hibernate.annotations.Index;

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
 * @param <T> The type of this l10n value.
 *
 * @author jnorris
 */
@MappedSuperclass
public abstract class L10nValue<T> extends TrackedEntity {

    public static final String PROPERTY_LOCALE = "locale";
    public static final String PROPERTY_VALUE = "value";

    private static final long serialVersionUID = 1L;

    private Locale locale;
    private T value;

    /**
     * @frameworkUseOnly
     */
    protected L10nValue() {
        super();
    }

    /**
     * @param locale
     *            The locale of this value.
     * @param value
     *            The value of the object.
     */
    protected L10nValue(Locale locale, T value) {
        this.locale = locale;
        this.value = value;
    }

    /**
     * @return The locale of this value.
     */
    @Column(name = "locale", nullable = false, updatable = false)
    @Index(name = "IDX_LOCALE")
    public Locale getLocale() {
        return locale;
    }

    /**
     * @frameworkUseOnly
     *
     * @param locale
     *            The locale of this value.
     */
    protected void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @requiresOrmOverride
     *
     * @return The value of the object.
     */
    @Transient
    public T getValue() {
        return value;
    }

    /**
     * @param value The value of the object.
     */
    public void setValue(T value) {
        this.value = value;
    }

}
