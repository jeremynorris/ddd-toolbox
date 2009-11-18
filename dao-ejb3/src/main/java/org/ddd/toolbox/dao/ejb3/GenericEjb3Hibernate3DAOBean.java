package org.ddd.toolbox.dao.ejb3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.ddd.toolbox.dao.BatchProcessCallbackEntity;
import org.ddd.toolbox.dao.BatchProcessCallbackEntitySet;
import org.ddd.toolbox.dao.BatchProcessCallbackEntitySingle;
import org.ddd.toolbox.dao.QueryFactoryCallback;
import org.ddd.toolbox.dao.QueryInitializeCallback;
import org.ddd.toolbox.ejb3.util.Ejb3Utils;
import org.ddd.toolbox.naturalkeytools.NaturalKeyTools;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.EntityManagerImpl;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.impl.SessionImpl;

/**
 * Implementation of the EJB3 specific DAO with hibernate extensions.
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
public abstract class GenericEjb3Hibernate3DAOBean<T_ENTITY, T_ENTITY_ID extends Serializable>
    extends GenericEjb3DAOBean<T_ENTITY, T_ENTITY_ID>
    implements GenericEjb3Hibernate3DAO<T_ENTITY, T_ENTITY_ID> {

    private static final int HIBERNATE_JDBC_BATCH_SIZE = 100;

    private static final Logger LOG = Logger.getLogger(GenericEjb3Hibernate3DAOBean.class);

    @EJB
    private PaginationProcessService paginationProcessService;

    /**
     * @see GenericEjb3DAOBean default constructor.
    protected GenericEjb3Hibernate3DAOBean() {
        super();
    }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdPropertyName() {
        return "id";
    }

    /**
     * @param persistenceClass
     *            The persistence class of this dao.
     * @param idClass
     *            The id class of this dao.
     */
    protected GenericEjb3Hibernate3DAOBean(Class<? extends T_ENTITY> persistenceClass, Class<T_ENTITY_ID> idClass) {
        super(persistenceClass, idClass);
    }

    /**
     * @return The hibernate session.
     */
    protected Session getHibernateSession() {

        Object delegate = getEntityManager().getDelegate();
        if (delegate instanceof SessionImpl) {
            // Spring/Hibernate
            return (Session) delegate;
        }
        else if (delegate instanceof EntityManagerImpl) {
            // JBoss 4.0.5
            return ((HibernateEntityManager) delegate).getSession();
        }

        throw new RuntimeException("unable to determine hibernate session.");

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY findById(T_ENTITY_ID id, LockMode lockMode) {
        return (T_ENTITY) getHibernateSession().get(getPersistenceClass(), id, lockMode);
    }
    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY saveOrUpdate(T_ENTITY entity) {
    	getHibernateSession().saveOrUpdate(entity);
    	return entity;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Set<T_ENTITY> findSetByIds(Set<T_ENTITY_ID> ids) {

        if (ids.isEmpty()) {
            return Collections.emptySet();
        }

        String parameterIds = "ids";
        String queryStr = "FROM" + " " + getPersistenceClass().getName() + " " + "WHERE" + " " + getIdPropertyName() + " " + "IN" + "(" + Ejb3Utils.parameterName(parameterIds) + ")";

        Query query = createHQLQuery(queryStr);
        query.setParameterList(parameterIds, ids);

        @SuppressWarnings("unchecked")
        Set<T_ENTITY> results = new HashSet<T_ENTITY>(query.list());

        return results;
    }
    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Map<T_ENTITY_ID, T_ENTITY> findMapByIds(Set<T_ENTITY_ID> ids) {
        
        Map<T_ENTITY_ID, T_ENTITY> results = new HashMap<T_ENTITY_ID, T_ENTITY>();
        Set<T_ENTITY> set = findSetByIds(ids);
        for (T_ENTITY entity : set) {
            // TODO: determine the how to safely find the ID accessor:
            throw new NotImplementedException("determine the how to safely find the ID accessor:");
        }
        
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected Set<T_ENTITY> findByIds(String idPropertyName, Set<T_ENTITY_ID> ids) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.in(idPropertyName, ids));
        @SuppressWarnings("unchecked")
        Set<T_ENTITY> results = new HashSet<T_ENTITY>(criteria.list());
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY findByNaturalKey(T_ENTITY entity) {
        return (T_ENTITY) NaturalKeyTools.getCriteriaNaturalKey(getHibernateSession(), entity).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY loadById(T_ENTITY_ID id) {
        return (T_ENTITY) getHibernateSession().load(getPersistenceClass(), id);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<T_ENTITY> findAll() {
        Criteria criteria = getCriteria();
        criteria.setComment(getClass().getSimpleName() + "::findAll()");
        return (List<T_ENTITY>) getCriteria().list();
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void refresh(T_ENTITY entity, LockMode lockMode) {
        getHibernateSession().refresh(entity, lockMode);
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected Criteria getCriteria() {
        Criteria criteria = getHibernateSession().createCriteria(getPersistenceClass());
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    /**
     * {@inheritDoc}
     */
    public Query createHQLQuery(String queryString) {
        return getHibernateSession().createQuery(queryString);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T_ENTITY> findByCriteria(org.hibernate.criterion.Criterion... criteria) {

        Criteria result = getCriteria();
        for (Criterion c : criteria) {
            result.add(c);
        }

        return result.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T_ENTITY> findByCriteria(Criteria criteria) {
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T_ENTITY findUniqueByCriteria(org.hibernate.criterion.Criterion... criteria) {

        Criteria result = getCriteria();
        for (Criterion c : criteria) {
            result.add(c);
        }
        return (T_ENTITY) result.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public T_ENTITY attach(T_ENTITY entity) {
        Session session = getHibernateSession();
        if (!session.contains(entity)) {
            return (T_ENTITY) session.merge(entity);
        }
        // TODO: switch this to something like this: getHibernateSession().lock(entity, LockMode.NONE);
        // However, it currently invoked some persist (even when persist is not
        // called) lifecycle operations on tx commit and needs investigation.
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public T_ENTITY initializeRelationships(T_ENTITY entity, String... properties) {
        try {
            for (String propertyName : properties) {
                Object proxy = PropertyUtils.getSimpleProperty(entity, propertyName);
                Hibernate.initialize(proxy);
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public Set<T_ENTITY> initializeRelationships(Set<T_ENTITY> entities, String... properties) {
        Set<T_ENTITY> results = new HashSet<T_ENTITY>();
        for (T_ENTITY entity : entities) {
            results.add(initializeRelationships(entity, properties));
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<T_ENTITY> findByExample(T_ENTITY exampleInstance, String... excludeProperties) {

        // Note: using Hibernate for this functionality since it's more
        // difficult with EntityManager and EJB-QL

        Criteria criteria = getCriteria();
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperties) {
            example.excludeProperty(exclude);
        }
        criteria.add(example);

        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    public int getEntityCount() {
        Criteria result = getCriteria();
        result.setProjection(Projections.rowCount());
        return ((Integer) result.list().get(0)).intValue();
    }



    /**
     * {@inheritDoc}
     */
    public int getEntityCountByCriteria(Criterion... criteria) {

        Criteria result = getCriteria();
        for (Criterion c : criteria) {
            result.add(c);
        }
        result.setProjection(Projections.rowCount());
        return ((Integer) result.list().get(0)).intValue();
    }

    /**
     * @obselete
     *
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int processAllBatchSingleScroll(Query query, BatchProcessCallbackEntitySingle<T_ENTITY> callback) throws IOException {

        Session session = getHibernateSession();

        // disable 2nd level cache:
        query.setCacheMode(CacheMode.IGNORE);

        ScrollableResults scroll = query.scroll();
        int count = 0;
        try {
            while (scroll.next()) {
                @SuppressWarnings("unchecked")
                T_ENTITY pkg = (T_ENTITY) scroll.get(0);
                callback.call(pkg);
                if (++count % HIBERNATE_JDBC_BATCH_SIZE == 0) {
                    session.flush();
                    session.clear();
                }
            }
        }
        finally {
            scroll.close();
        }

        return count;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int processAllBatchSingle(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySingle<T_ENTITY> callback) {
        return processAllBatchHelper(queryFactoryCallback, null, paginationSize, detach, (BatchProcessCallbackEntity<T_ENTITY>)callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int processAllBatchSingle(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySingle<T_ENTITY> callback) throws IllegalArgumentException {
        // Note: initializeCallback is explicitly checked since the present of
        // nulls is meaningful in the next call, processAllBatchHelper():
        if (initializeCallback == null) {
            throw new IllegalArgumentException("initializeCallback must not be null");
        }
        return processAllBatchHelper(queryFactoryCallback, initializeCallback, paginationSize, detach, (BatchProcessCallbackEntity<T_ENTITY>)callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int processAllBatchSet(QueryFactoryCallback<Query> queryFactoryCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySet<T_ENTITY> callback) {
        return processAllBatchHelper(queryFactoryCallback, null, paginationSize, detach, (BatchProcessCallbackEntity<T_ENTITY>) callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int processAllBatchSet(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, boolean detach, BatchProcessCallbackEntitySet<T_ENTITY> callback) {
        // Note: initializeCallback is explicitly checked since the present of
        // nulls is meaningful in the next call, processAllBatchHelper():
        if (initializeCallback == null) {
            throw new IllegalArgumentException("initializeCallback must not be null");
        }
        return processAllBatchHelper(queryFactoryCallback, initializeCallback, paginationSize, detach, (BatchProcessCallbackEntity<T_ENTITY>) callback);
    }

    private int processAllBatchHelper(QueryFactoryCallback<Query> queryFactoryCallback, QueryInitializeCallback<T_ENTITY> initializeCallback, int paginationSize, boolean detach, BatchProcessCallbackEntity<T_ENTITY> callback) {

        boolean resultsAvailable = true;
        int startIndex = 0;
        int total = 0;
        while (resultsAvailable) {
            Set<T_ENTITY> entities = Collections.emptySet();
            if (detach) {
                entities = this.paginationProcessService.executeQueryList(queryFactoryCallback, initializeCallback, paginationSize, startIndex);
            }
            if (callback instanceof BatchProcessCallbackEntitySingle) {
                BatchProcessCallbackEntitySingle<T_ENTITY> callbackSingle = (BatchProcessCallbackEntitySingle<T_ENTITY>) callback;
                if (detach) {
                    for (T_ENTITY entity : entities) {
                        this.paginationProcessService.processSingle(callbackSingle, entity);
                    }
                }
                else {
                    entities = this.paginationProcessService.executeQueryListAndProcessSingle(queryFactoryCallback, initializeCallback, paginationSize, startIndex, callbackSingle);
                }
            }
            else if (callback instanceof BatchProcessCallbackEntitySet) {
                BatchProcessCallbackEntitySet<T_ENTITY> callbackSet = (BatchProcessCallbackEntitySet<T_ENTITY>) callback;
                if (detach) {
                    this.paginationProcessService.processSet(callbackSet, entities);
                }
                else {
                    entities = this.paginationProcessService.executeQueryListAndProcessSet(queryFactoryCallback, initializeCallback, paginationSize, startIndex, callbackSet);
                }
            }
            else {
                assert false : "Unknown " + BatchProcessCallbackEntity.class.getName() + " type.";
            }

            int count = entities.size();
            total += count;
            LOG.info("processAllBatch: processed " + count + " entities in batch, processed " + total + " entities in total.");
            if (count > 0) {
                if (count == paginationSize) {
                    startIndex += paginationSize;
                }
                else {
                    // We had less results than the pagination size, therefore
                    // we are at the end.
                    resultsAvailable = false;
                }
            }
            else {
                resultsAvailable = false;
            }
        }

        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushPersistenceContext() {
        getHibernateSession().flush();
    }

}
