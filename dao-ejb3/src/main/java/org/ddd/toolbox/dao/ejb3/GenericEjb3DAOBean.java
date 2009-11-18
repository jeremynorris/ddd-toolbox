package org.ddd.toolbox.dao.ejb3;

import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

/**
 * Implementation of the EJB3 specific DAO.
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
public abstract class GenericEjb3DAOBean<T_ENTITY, T_ENTITY_ID extends Serializable> implements GenericEjb3DAO<T_ENTITY, T_ENTITY_ID> {

    private Class<? extends T_ENTITY> persistenceClass;
    private Class<T_ENTITY_ID> idClass;

    private EntityManager entityManager;

    /**
     * This constructor can be used when the concrete DAOs direct superclass
     * provides two generic variables, the persistence class and the id class
     * respectively, and are the first two generic variable defined. More
     * complex hierarchies that define one dao aspect higher up, or other generic
     * elements must be use the explicit constructor and specify both the
     * persistence class and the id class. For more information on this
     * "limitation", see java/lang/Class.html#getGenericSuperclass().
    protected GenericEjb3DAOBean() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        Class<T_ENTITY> proposedPersistenceClass = null;
        Class<T_ENTITY_ID> proposedIdClass = null;
        if (actualTypeArguments.length > 1) {
            proposedPersistenceClass = (Class<T_ENTITY>) actualTypeArguments[0];
            proposedIdClass = (Class<T_ENTITY_ID>) actualTypeArguments[1];
        }
        else {
            throw new RuntimeException("Unable to determine generic class instances for: " + getClass());
        }

        this.persistenceClass = proposedPersistenceClass;
        this.idClass = proposedIdClass;
    }
     */

    /**
     * @param persistenceClass
     *            The persistence class of this dao.
     * @param idClass
     *            The id class of this dao.
     */
    protected GenericEjb3DAOBean(Class<? extends T_ENTITY> persistenceClass, Class<T_ENTITY_ID> idClass) {
        this.persistenceClass = persistenceClass;
        this.idClass = idClass;
    }

    /**
     * @return The persistence type that this DAO concerns.
     */
    protected Class<? extends T_ENTITY> getPersistenceClass() {
        return persistenceClass;
    }

    /**
     * @return The type of the primary data storage that this DAO concerns.
     */
    protected Class<T_ENTITY_ID> getIdClass() {
        return idClass;
    }

    /**
     * @requiresOverrideForInjection to inject the desired @PersistenceContext.
     *
     * @return The entity manager.
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * {@inheritDoc}
     */
    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY findById(T_ENTITY_ID id) {
        return this.entityManager.find(getPersistenceClass(), id);
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void refresh(T_ENTITY entity) {
        this.entityManager.refresh(entity);
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY makePersistent(T_ENTITY entity) {
        this.entityManager.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY merge(T_ENTITY entity) {
        return this.entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T_ENTITY makeTransient(T_ENTITY entity) {
        entity = entityManager.merge(entity);
        this.entityManager.remove(entity);
        return entity;
    }

}