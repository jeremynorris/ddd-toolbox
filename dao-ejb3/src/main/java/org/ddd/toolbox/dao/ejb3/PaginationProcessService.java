package org.ddd.toolbox.dao.ejb3;

import java.util.Set;

import javax.ejb.Local;

import org.ddd.toolbox.dao.BatchProcessCallbackEntitySet;
import org.ddd.toolbox.dao.BatchProcessCallbackEntitySingle;
import org.ddd.toolbox.dao.QueryFactoryCallback;
import org.ddd.toolbox.dao.QueryInitializeCallback;
import org.hibernate.Query;

/**
 * PaginationProcessService provides pagination services (ie: transaction demarcation
 * boundaries for pagination processing operations on entities).
 *
 * This class is currently tied to Hibernate 3.
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions: jnorris: Aug 20, 2008: Initial revision.
 *
 * @author jnorris
 */
@Local
public interface PaginationProcessService {

    /**
     * Perform entity processing operations on single entity.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param callback
     *            The callback to perform on the entity.
     * @param entity
     *            The entity to process.
     */
    public <T_ENTITY> void processSingle(BatchProcessCallbackEntitySingle<T_ENTITY> callback, T_ENTITY entity);

    /**
     * Perform entity processing operations on an entity set.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param callback
     *            The callback to perform on the entity set.
     * @param entities
     *            The entities to process.
     */
    public <T_ENTITY> void processSet(BatchProcessCallbackEntitySet<T_ENTITY> callback, Set<T_ENTITY> entities);

    /**
     * Execute a query (list operation).
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryList(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex);

    /**
     * Execute a query (list operation).
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param initializeCallback
     *            The callback to use to initialize the query results.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryList(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex);

    /**
     * Execute a query (list operation) and perform entity processing operations
     * on single entity.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @param callback
     *            The callback to perform on each individual entity.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSingle(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySingle<T_ENTITY> callback);

    /**
     * Execute a query (list operation) and perform entity processing operations
     * on single entity.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param initializeCallback
     *            The callback to use to initialize the query results.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @param callback
     *            The callback to perform on each individual entity.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSingle(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySingle<T_ENTITY> callback);

    /**
     * Execute a query (list operation) and perform entity processing operations
     * on an entity set.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @param callback
     *            The callback to perform on the entity set.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSet(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySet<T_ENTITY> callback);

    /**
     * Execute a query (list operation) and perform entity processing operations
     * on an entity set.
     *
     * @param <T_ENTITY>
     *            The type of entity being operated on.
     * @param queryFactoryCallback
     *            The callback factory to build the query to execute.
     * @param initializeCallback
     *            The callback to use to initialize the query results.
     * @param paginationSize
     *            The max results to set on the query.
     * @param startIndex
     *            The start index to set on the query.
     * @param callback
     *            The callback to perform on the entity set.
     * @return The resulting set of entities.
     */
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSet(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySet<T_ENTITY> callback);

}
