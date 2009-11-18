package org.ddd.toolbox.ejb3.entityreport;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.ddd.toolbox.ejb3.util.CopyEntityPersist;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @is.immutable
 * 
 * @author jeremy.norris
 */
@MappedSuperclass
public abstract class EntityErrorDetail<T extends ReportEntity> implements CopyEntityPersist<EntityErrorDetail<T>> {
    
    private int id;
    private T entity;
    private DateTime dateTime;
    private Integer rc;
    private String message;

    /**
     * @frameworkUseOnly
     */
    protected EntityErrorDetail() {
    }

    /**
     * @param entity
     *            The entity which the error detail is about.
     * @param dateTime
     *            The datetime of the error detail.
     * @param message
     *            The message of the error detail.
     * @param rc
     *            The return code of the error detail (nullable).
     */
    protected EntityErrorDetail(T entity, DateTime dateTime, String message, Integer rc) {
        this.entity = entity;
        this.dateTime = dateTime;
        this.message = message;
        this.rc = rc;
    }

    /**
     * @return
     */
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    /**
     * @requiresOrmOverride
     */
    @Transient
    public T getEntity() {
        return entity;
    }

    protected void setEntity(T entity) {
        this.entity = entity;
    }
    
    @Type(type="dateTime")
    @Column(name = "date_time", nullable = false, updatable = false)
    public DateTime getDateTime()
    {
        return dateTime;
    }

    protected void setDateTime(DateTime datetime)
    {
        this.dateTime = datetime;
    }

    /**
     * @return The name of this object.
     */
    @Column(name="rc", nullable=true, updatable=false)
    public Integer getRc()
    {
        return rc;
    }

    /**
     * @param message The name of this object.
     */
    protected void setRc(Integer rc) {
        this.rc = rc;
    }
    
    /**
     * @return The name of this object.
     */
    @Column(name="message", nullable=false, updatable=false)
    public String getMessage() {
        return message;
    }

    /**
     * @param message The name of this object.
     */
    protected void setMessage(String errorMessage) {
        this.message = errorMessage;
    }
    
    /**
     * Produce a String representation of this object. This method is not
     * recommended for programmatic use.
     * 
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + ": " + getMessage() + "]";
    }    
}
