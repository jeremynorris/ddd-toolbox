package org.ddd.toolbox.dao;

import java.util.Set;

/**
 * Callback interface for initializing query entities.
 *
 * <br>
 * Patterns: Callback
 *
 * <br>
 * Revisions: jnorris: Oct 2, 2007: Initial revision.
 *
 * @param <T_ENTITY>
 *            The type of the entities being initialized.
 *
 * @author jnorris
 */
public interface QueryInitializeCallback<T_ENTITY> {

    /**
     * Initialize the entities.
     *
     * @param entities
     *            The entities to be initialized.
     * @return The resulting initialized entities.
     */
    public Set<T_ENTITY> initialize(Set<T_ENTITY> entities);
}
