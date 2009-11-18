package org.ddd.toolbox.dao.ejb3;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ddd.toolbox.dao.BatchProcessCallbackEntitySet;
import org.ddd.toolbox.dao.BatchProcessCallbackEntitySingle;
import org.ddd.toolbox.dao.QueryFactoryCallback;
import org.ddd.toolbox.dao.QueryInitializeCallback;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * An EJB3 with Hibernate 3.x specific extensions DAO interface.
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions:
 * jeremy.norris: Feb 23, 2007: Initial revision.
 *
 * @param <T_ENTITY>
 *            The object that this DAO concerns.
 * @param <T_ENTITY_ID>
 *            The primary data storage identity of the object that this DAO
 *            concerns.
 *
 * @author jeremy.norris
 */
public interface GenericEjb3Hibernate3DAO<T_ENTITY, T_ENTITY_ID extends Serializable> extends GenericEjb3DAO<T_ENTITY, T_ENTITY_ID> {

    /**
     * @return The property name of the id field.
     */
    public String getIdPropertyName();

    /**
     * Create an HQL query.
     *
     * @param queryString
     *            The query to use.
     * @return The HQL query.
     */
    public Query createHQLQuery(String queryString);

    /**
     * @param id The identity of the object to find.
     * @param lockMode The lockMode to use on the entity.
     *
     * @return The attached result entity, null if it does not exist.
     */
    public T_ENTITY findById(T_ENTITY_ID id, LockMode lockMode);

    /**
     * Find set of entities by id.
     *
     * @param ids
     *            The ids to retrieve.
     * @return The set of entities, if there is no entity
     *         corresponding to a given id, it will not be included in the resulting set.
     */
    public Set<T_ENTITY> findSetByIds(Set<T_ENTITY_ID> ids);
    
    /**
     * Find map of entities by id.
     * 
     * @param ids
     *            The ids to retrieve.
     * @return The map of entities hashed by their id, if there is no entity
     *         corresponding to a given id, it will not be included in the
     *         resulting map.
     */
    public Map<T_ENTITY_ID, T_ENTITY> findMapByIds(Set<T_ENTITY_ID> ids);

    /**
     * @param id The identity of the object to load.
     *
     * @return The attached result entity, null if it does not exist.
     */
    public T_ENTITY loadById(T_ENTITY_ID id);
    
    /**
     * 
     * @param entity the entity to persist
     * @return the persisted entity
     */
    public T_ENTITY saveOrUpdate(T_ENTITY entity);

    /**
     * @param criteria varargs array of criterion objects for this query.
     *
     * @return The resulting entities;
     */
    public List<T_ENTITY> findByCriteria(org.hibernate.criterion.Criterion... criteria);


    /**
     * for use when other aspects of the criteria are filled in by the dao. Allows for Type conversion.
     * @param criteria the criteria to list
     * @return the list
     */
    public List<T_ENTITY> findByCriteria(Criteria criteria);

    /**
     * Find an entity by example.
     *
     * @param exampleInstance The example instance to find instances like.
     * @param excludeProperties Properties to exclude from the example likeness.
     *
     * @return A list of like entities.
     */
    public List<T_ENTITY> findByExample(T_ENTITY exampleInstance, String... excludeProperties);

    /**
     * @param criterion
     *            varargs array of criterion objects for this query.
     *
     * @return A single entity that matches the query, or null if the query
     *         returns no results.
     */
    public T_ENTITY findUniqueByCriteria(org.hibernate.criterion.Criterion... criteria);

    /**
     * Refresh a managed entity.
     *
     * @param lockMode The lockMode to use on the entity.
     * @param entity The entity to be refreshed.
     */
    public void refresh(T_ENTITY entity, LockMode lockMode);

    /**
     * @return The number of persistent entities in the persistence storage area.
     */
    public int getEntityCount();

    /**
     * Attach an entity to the persistence context if it's not already attached.
     *
     * @param entity
     *            The entity to attach.
     * @return The attached entity.
     */
    public T_ENTITY attach(T_ENTITY entity);

    /**
     * Ensures the properties of the provided entity are initialized. If the
     * entity is not attached, it will be attached and the new attached entity
     * will be returned.
     *
     * @param entity
     *            The entity to initialize relationships with.
     * @param properties
     *            The properties to initialize.
     * @return The attached entity with properties initialized.
     */
    public T_ENTITY initializeRelationships(T_ENTITY entity, String... properties);

    /**
     * Ensures the properties of the provided entity are initialized. If the
     * entity is not attached, it will be attached and the new attached entity
     * will be returned.
     *
     * Note: This call needs to be used with caution to avoid performance
     * problems. For large set, ensure they are already attached and use @BatchSize
     * batch fetching.
     *
     * @param entity
     *            The entity to initialize relationships with.
     * @param properties
     *            The properties to initialize.
     * @return The attached entity with properties initialized.
     */
    public Set<T_ENTITY> initializeRelationships(Set<T_ENTITY> entity, String... properties);

    /**
     * @param criteria varargs array of criterion objects.
     *
     * @return The number of persistent entities in the persistence storage area for the given criteria.
     */
    public int getEntityCountByCriteria(org.hibernate.criterion.Criterion... criteria);

    /**
     * @obselete Process all entities in batch. Caution all processing occurs
     *           within one transaction. Main benefit is memory reduction.
     *           processAllBatchSingle() is almost always more appropriate.
     *
     * @param query
     *            The query to execute in batch.
     * @param callback
     *            The callback to execute for each pkg.
     * @return The number of entities processed.
     * @throws IOException
     *             An IO exception occurred.
     */
    public int processAllBatchSingleScroll(Query query, BatchProcessCallbackEntitySingle<T_ENTITY> callback) throws IOException;

    /**
     * Process entities in batches of size paginationSize. Each entity is
     * processed individually with the provided callback. Transaction boundaries
     * are around each batch of size paginationSize.
     *
     * @param queryFactoryCallback
     *            The factory callback to create the query representing the
     *            entire scope of work.
     * @param paginationSize
     *            The pagination size.
     * @param detach
     *            Whether or not to detach the objects between the batch query
     *            and callback processing.
     * @param callback
     *            The callback to process each entity with individually.
     * @return The total number of entities processed.
     */
    public int processAllBatchSingle(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySingle<T_ENTITY> callback);

    /**
     * Process entities in batches of size paginationSize. Each entity is
     * processed individually with the provided callback. Transaction boundaries
     * are around each batch of size paginationSize.
     *
     * @param queryFactoryCallback
     *            The factory callback to create the query representing the
     *            entire scope of work.
     * @param initializeCallback
     *            The callback to use to initialize the query results for each
     *            batch.
     * @param paginationSize
     *            The pagination size.
     * @param detach
     *            Whether or not to detach the objects between the batch query
     *            and callback processing.
     * @param callback
     *            The callback to process each entity with individually.
     * @return The total number of entities processed.
     */
    public int processAllBatchSingle(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySingle<T_ENTITY> callback);

    /**
     * Process entities in batches of size paginationSize. Each entity set is
     * processed with the provided callback. Transaction boundaries are around
     * each batch of size paginationSize.
     *
     * @param queryFactoryCallback
     *            The factory callback to create the query representing the
     *            entire scope of work.
     * @param paginationSize
     *            The pagination size.
     * @param detach
     *            Whether or not to detach the objects between the batch query
     *            and callback processing.
     * @param callback
     *            The callback to process each entity set.
     * @return The total number of entities processed.
     */
    public int processAllBatchSet(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySet<T_ENTITY> callback);

    /**
     * Process entities in batches of size paginationSize. Each entity set is
     * processed with the provided callback. Transaction boundaries are around
     * each batch of size paginationSize.
     *
     * @param queryFactoryCallback
     *            The factory callback to create the query representing the
     *            entire scope of work.
     * @param initializeCallback
     *            The callback to use to initialize the query results for each
     *            batch.
     * @param paginationSize
     *            The pagination size.
     * @param detach
     *            Whether or not to detach the objects between the batch query
     *            and callback processing.
     * @param callback
     *            The callback to process each entity set.
     * @return The total number of entities processed.
     */
    public int processAllBatchSet(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySet<T_ENTITY> callback);

    /**
     *
     */
    public void flushPersistenceContext();
}
