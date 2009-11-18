package org.ddd.toolbox.dao;

/**
 * BatchProcessCallbackEntitySingle
 *
 * <br>
 * Patterns: Callback
 *
 * <br>
 * Revisions: jnorris: Oct 2, 2007: Initial revision.
 *
 * @param <T_ENTITY>
 *            The type of the entity this callback concerns.
 *
 * @author jnorris
 */
public interface BatchProcessCallbackEntitySingle<T_ENTITY> extends BatchProcessCallbackEntity<T_ENTITY> {

    /**
     * Execute callback.
     *
     * @param entity
     *            The entity which this callback concerns.
     */
    public void call(T_ENTITY entity);
}
