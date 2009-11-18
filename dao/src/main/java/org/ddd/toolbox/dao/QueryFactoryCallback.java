package org.ddd.toolbox.dao;

/**
 * Callback interface for building queries.
 *
 * <br>
 * Patterns: Callback
 *
 * <br>
 * Revisions: jnorris: Oct 2, 2007: Initial revision.
 *
 * @param <T_QUERY>
 *            The type of the query being constructed.
 *
 * @author jnorris
 */
public interface QueryFactoryCallback<T_QUERY> {

    /**
     * Build the query.
     *
     * @return The resulting query.
     */
    public T_QUERY build();
}
