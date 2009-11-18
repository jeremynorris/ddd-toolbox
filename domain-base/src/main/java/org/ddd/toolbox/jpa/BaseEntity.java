package org.ddd.toolbox.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Provides a base entity for class for a standard surrogate id key.
 * 
 * <br>
 * Patterns:
 * 
 * <br>
 * Revisions: jeremy.norris: Oct 23, 2006: Initial revision.
 * 
 * @author jeremy.norris
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    
    public static final String PROPERTY_ID = "id";
    
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * @frameworkUseOnly
     */
    protected BaseEntity() {
    }

    /**
     * @return The persistence entity of this object.
     */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            The persistence entity of this object.
     */
    protected void setId(Integer id) {
        this.id = id;
    }
    
}
