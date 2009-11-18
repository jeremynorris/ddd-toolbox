package org.ddd.toolbox.jpa.l10n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.LocaleUtils;
import org.ddd.toolbox.jpa.BaseEntity;

/**
 * L10nBundle
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions: jnorris: Sep 11, 2008: Initial revision.
 *
 * @param <T_PARENT>
 *            The type of the parent of this bundle.
 * @param <T_VALUE>
 *            The type of the l10n value.
 * @param <T_L10N_VALUE>
 *            The type of the 110n value wrapper.
 *
 * @author jnorris
 */
public abstract class L10nBundle<T_PARENT, T_VALUE, T_L10N_VALUE extends L10nValue<T_VALUE>> extends BaseEntity {

    public static final String PROPERTY_PARENT = "parent";
    public static final String PROPERTY_ENTRIES = "entries";

    private static final long serialVersionUID = 1L;

    private T_PARENT parent;
    private Map<Locale, T_L10N_VALUE> entries;

    /**
     * @frameworkUseOnly
     */
    protected L10nBundle() {
        super();
        this.entries = new HashMap<Locale, T_L10N_VALUE>();
    }

    /**
     * @param parent The parent of this bundle.
     */
    protected L10nBundle(T_PARENT parent) {
        super();
        this.parent = parent;
        this.entries = new HashMap<Locale, T_L10N_VALUE>();
    }

    /**
     * @requiresOrmOverride
     *
     * @return The parent of this bundle.
     */
    @Transient
    public T_PARENT getParent() {
        return parent;
    }

    /**
     * @requiresOrmOverride
     *
     * @param parent
     *            The parent of this bundle.
     */
    public void setParent(T_PARENT parent) {
        this.parent = parent;
    }

    /**
     * @requiresOrmOverride
     *
     * @return The l10n entries for this bundle.
     */
    @Transient
    protected Map<Locale, T_L10N_VALUE> getEntries() {
        return entries;
    }

    /**
     * @requiresOrmOverride
     *
     * @param entries
     *            The l10n entries for this bundle.
     */
    @Transient
    protected void setEntries(Map<Locale, T_L10N_VALUE> entries) {
        this.entries = entries;
    }

    /**
     * Get the locale entry for the specified locale.
     *
     * @param locale
     *            The locale.
     * @return The localized entry for the provided locale, null if it does not
     *         exist.
     */
    @Transient
    public T_L10N_VALUE getEntry(Locale locale) {
        return getEntries().get(locale);
    }

    /**
     * Remove the locale entry for the specified locale.
     *
     * @param locale
     *            The locale.
     */
    public void removeEntry(Locale locale) {
        getEntries().remove(locale);
    }

    /**
     * Get the locale entry for the specified locale.
     *
     * @param locale
     *            The locale.
     * @return The localized entry for the provided locale or any of the
     *         appropriate fallback locales if it doesn't exist, null if no
     *         appropriate entries exist.
     */
    @Transient
    public T_L10N_VALUE getEntryWithFallback(Locale locale) {
        T_L10N_VALUE result = null;

        List<Locale> localeLookupList = new ArrayList<Locale>();
        @SuppressWarnings("unchecked")
        List<Locale> localeLookupListInitial = LocaleUtils.localeLookupList(locale);
        localeLookupList.addAll(localeLookupListInitial);
        if (locale != Locale.ENGLISH) {
            @SuppressWarnings("unchecked")
            List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH);
            localeLookupList.addAll(list);
        }
        if (locale != Locale.CHINESE) {
            @SuppressWarnings("unchecked")
            List<Locale> list = LocaleUtils.localeLookupList(Locale.CHINESE);
            localeLookupList.addAll(list);
        }

        for (Locale localeCandidate : localeLookupList) {
            result = getEntries().get(localeCandidate);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    /**
     * Get the value for the specified locale.
     *
     * @param locale
     *            The locale to requested.
     * @return The localized entry for the provided locale, null if it does not
     *         exist.
     */
    @Transient
    public T_VALUE get(Locale locale) {
        T_VALUE result = null;
        T_L10N_VALUE entry = getEntry(locale);
        if (entry != null) {
            result = entry.getValue();
        }
        return result;
    }

    /**
     * Get the value for the specified locale.
     *
     * @param locale
     *            The locale to requested.
     * @return The localized value for the provided locale or any of the
     *         appropriate fallback locales if it doesn't exist, null if no
     *         appropriate entries exist.
     */
    @Transient
    public T_VALUE getWithFallback(Locale locale) {
        T_VALUE result = null;
        T_L10N_VALUE entry = getEntryWithFallback(locale);
        if (entry != null) {
            result = entry.getValue();
        }
        return result;
    }

    /**
     * Add an entry to a bundle.
     *
     * @param entry
     *            The l10n entry to add to the bundle.
     */
    public void putEntry(T_L10N_VALUE entry) {
        getEntries().put(entry.getLocale(), entry);
    }

    /**
     * Add an entry to a bundle. This is a helper method for use by concrete
     * subclasses implementing put(Locale locale, T_VALUE value).
     *
     * @param locale
     *            The locale of the entry to add.
     * @param value
     *            The value to add.
     * @param clazz
     *            The class to instantiate.
     * @param immutableEntry
     *            Whether or not the entry is immutable, only applicable on
     *            entry updates and determines whether the existing entry is
     *            updated or a new entry is created.
     */
    protected void put(Locale locale, T_VALUE value, Class<T_L10N_VALUE> clazz, boolean immutableEntry) {

        boolean createNewEntry = false;
        T_L10N_VALUE existingEntry = getEntries().get(locale);
        if (existingEntry == null) {
            createNewEntry = true;
        }
        else {
            // Handle updates:
            if (immutableEntry) {
                createNewEntry = true;
            }
            else {
                existingEntry.setValue(value);
            }
        }

        if (createNewEntry) {
            T_L10N_VALUE entry = L10nBundleFactory.createBundleEntry(clazz);
            entry.setLocale(locale);
            entry.setValue(value);
            getEntries().put(locale, entry);
        }

    }

    /**
     * Update an entry to a bundle (if the entries are mutable).
     *
     * @param locale
     *            The locale of the entry to update.
     * @param value
     *            The value to update.
     * @throws IllegalArgumentException
     *             If an entry for the provided locale does not exist.
     */
    public void update(Locale locale, T_VALUE value) throws IllegalArgumentException {
        T_L10N_VALUE existingEntry = getEntries().get(locale);
        if (existingEntry != null) {
            existingEntry.setValue(value);
        }
        else {
            throw new IllegalArgumentException("l10n entry for '" + locale + "' does not exist.");
        }
    }

    /**
     * Add an entry to a bundle.
     *
     * @param locale
     *            The locale of the entry to add.
     * @param value
     *            The value to add.
     */
    public abstract void put(Locale locale, T_VALUE value);

}
