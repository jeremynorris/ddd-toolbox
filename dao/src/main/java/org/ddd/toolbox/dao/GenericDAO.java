package org.ddd.toolbox.dao;

import java.io.Serializable;
import java.util.List;

/**
 * This is a generic DAO interface.
 *
 * Note: This interface was initially created for and has been heavily
 * influenced by hibernate and JPA persistence architectures and the hibernate
 * generic DAO pattern (http://www.hibernate.org/328.html), therefore the
 * abstraction may be completely unfit for other persistence strategies (ie:
 * JDO).
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions: jeremy.norris: Feb 23, 2007: Initial revision.
 *
 * @param <T_ENTITY>
 *            The object that this DAO concerns.
 * @param <T_ENTITY_ID>
 *            The primary data storage identity of the object that this DAO
 *            concerns.
 *
 * @author jeremy.norris
 */
public interface GenericDAO<T_ENTITY, T_ENTITY_ID extends Serializable> {

    /**
     * @param id The identity of the object to find.
     *
     * @return The attached result entity, null if it does not exist.
     */
    public T_ENTITY findById(T_ENTITY_ID id);

    /**
     * Find the entity that has the primary key of the provided example entity.
     *
     * @param exampleEntity The example entity.
     *
     * @return The attached result entity, null if it does not exist.
     */
    public T_ENTITY findByNaturalKey(T_ENTITY exampleEntity);

    /**
     * @return All entities of this DAO type.
     */
    public List<T_ENTITY> findAll();

    /**
     * Make an unmanaged entity persistent.
     *
     * @param entity The unmanaged entity to make persistent.
     *
     * @return The same entity provided (possibly mutated by persistence implementation).
     */
    public T_ENTITY makePersistent(T_ENTITY entity);

    /**
     * Merge a detached entity into persistence storage.
     *
     * @param entity The detached entity to be merged.
     *
     * @return The attached entity after being made persistent.
     */
    public T_ENTITY merge(T_ENTITY entity);

    /**
     * Make a detached entity transient (ie: remove it from persistence
     * storage).
     *
     * @param entity
     *            The detached entity to be removed.
     *
     * @return The same entity provided (possibly mutated, becoming unmanaged).
     */
    public T_ENTITY makeTransient(T_ENTITY entity);

    /**
     * Refresh a managed entity.
     *
     * @param entity The entity to be refreshed.
     */
    public void refresh(T_ENTITY entity);

}
