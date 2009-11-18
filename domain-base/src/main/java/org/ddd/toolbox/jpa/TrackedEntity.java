package org.ddd.toolbox.jpa;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Provides a base entity for class for a standard surrogate id key.
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions: jeremy.norris: Oct 24, 2006: Initial revision.
 *
 * @author jeremy.norris
 */
@MappedSuperclass
@EntityListeners(TrackedEntityListener.class)
public abstract class TrackedEntity extends BaseEntity {

    public static final String PROPERTY_DTS_INSERT = "dtsInsert";
    public static final String PROPERTY_DTS_UPDATE = "dtsUpdate";

    private DateTime dtsInsert;
    private DateTime dtsUpdate;

    /**
     * @frameworkUseOnly
     */
    protected TrackedEntity() {
        super();
    }

    /**
     * @return The insertion datestamp of this entity.
     */
    @Type(type="dateTime")
    @Column(name = "dts_insert", nullable = false, updatable = false)
    public DateTime getDtsInsert() {
        return dtsInsert;
    }

    /**
     * @frameworkUseOnly
     *
     * @param dtsInsert The insertion datestamp of this entity.
     */
    protected void setDtsInsert(DateTime dtsInsert) {
        this.dtsInsert = dtsInsert;
    }

    /**
     * @return The update datestamp of this entity.
     */
    @Type(type="dateTime")
    @Column(name = "dts_update", nullable = false)
    public DateTime getDtsUpdate() {
        return dtsUpdate;
    }

    /**
     * @frameworkUseOnly
     *
     * @param dtsUpdate The update datestamp of this entity.
     */
    protected void setDtsUpdate(DateTime dtsUpdate) {
        this.dtsUpdate = dtsUpdate;
    }
}
