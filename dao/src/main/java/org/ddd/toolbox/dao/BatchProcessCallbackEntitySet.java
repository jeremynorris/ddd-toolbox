package org.ddd.toolbox.dao;

import java.util.Set;

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
public interface BatchProcessCallbackEntitySet<T_ENTITY> extends BatchProcessCallbackEntity<T_ENTITY> {

    /**
     * Execute callback.
     *
     * @param entities
     *            The set of entities which is the callback concerns.
     */
    public void call(Set<T_ENTITY> entities);


}
