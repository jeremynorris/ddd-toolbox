package org.ddd.toolbox.dao.ejb3;

import java.io.Serializable;

import org.ddd.toolbox.dao.GenericDAO;

/**
 * An EJB3 specific DAO interface.
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
public interface GenericEjb3DAO<T_ENTITY, T_ENTITY_ID extends Serializable> extends GenericDAO<T_ENTITY, T_ENTITY_ID> {
}
