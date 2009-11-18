package org.ddd.toolbox.dao.ejb3;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.ddd.toolbox.dao.BatchProcessCallbackEntitySet;
import org.ddd.toolbox.dao.BatchProcessCallbackEntitySingle;
import org.ddd.toolbox.dao.QueryFactoryCallback;
import org.ddd.toolbox.dao.QueryInitializeCallback;
import org.hibernate.Query;

/**
 * PaginationProcessServiceBean
 *
 * <br>
 * Patterns:
 *
 * <br>
 * Revisions:
 * jnorris: Aug 20, 2008: Initial revision.
 *
 * @author jnorris
 */
@Stateless
public class PaginationProcessServiceBean implements PaginationProcessService {

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> void processSingle(BatchProcessCallbackEntitySingle<T_ENTITY> callback, T_ENTITY entity) {
        callback.call(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> void processSet(BatchProcessCallbackEntitySet<T_ENTITY> callback, Set<T_ENTITY> entities) {
        callback.call(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryList(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex) {
        return executeQueryListHelper(queryFactoryCallback, null, paginationSize, startIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryList(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex) {
        return executeQueryListHelper(queryFactoryCallback, initializeCallback, paginationSize, startIndex);
    }

    private <T_ENTITY> Set<T_ENTITY> executeQueryListHelper(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex) {

        Query query = queryFactoryCallback.build();
        query.setMaxResults(paginationSize);
        query.setFirstResult(startIndex);

        @SuppressWarnings("unchecked")
        Set<T_ENTITY> results = new HashSet<T_ENTITY>(query.list());

        if (initializeCallback != null) {
            results = initializeCallback.initialize(results);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSingle(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySingle<T_ENTITY> callback) {
        Set<T_ENTITY> entities = executeQueryList(queryFactoryCallback, paginationSize, startIndex);
        for (T_ENTITY entity : entities) {
            processSingle(callback, entity);
        }
        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSingle(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySingle<T_ENTITY> callback) {
        Set<T_ENTITY> entities = executeQueryList(queryFactoryCallback, initializeCallback, paginationSize, startIndex);
        for (T_ENTITY entity : entities) {
            processSingle(callback, entity);
        }
        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSet(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySet<T_ENTITY> callback) {
        Set<T_ENTITY> entities = executeQueryList(queryFactoryCallback, paginationSize, startIndex);
        processSet(callback, entities);
        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T_ENTITY> Set<T_ENTITY> executeQueryListAndProcessSet(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, int startIndex, BatchProcessCallbackEntitySet<T_ENTITY> callback) {
        Set<T_ENTITY> entities = executeQueryList(queryFactoryCallback, initializeCallback, paginationSize, startIndex);
        processSet(callback, entities);
        return entities;
    }

}
