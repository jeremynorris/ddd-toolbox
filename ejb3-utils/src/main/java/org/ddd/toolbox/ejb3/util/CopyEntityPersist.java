package org.ddd.toolbox.ejb3.util;

/**
 * This interface requires implementation for copying the value portion of an
 * entity. Anything that is generated, for example, anything injected from the
 * database (ie: &#64;GeneratedValue, &#64;Version, etc.) are not copied (reset
 * to null). "Persist" refers to the behavior that this copy should be
 * transitively deep across CascadeType.PERSIST entity relationships.
 * 
 * One use of this interface is to provide a safe way to copy a entity after a
 * persist transaction fails for the purpose of transaction retrying.
 * 
 * @author jeremy.norris
 * 
 * @param <T>
 *            The type of the target of the copyable entity.
 */
public interface CopyEntityPersist<T>
{
    /**
     * This should ensure that this object is copied deeply as well as any
     * CascadeType.PERSIST relationships. Everything else should be shallow.
     * 
     * @return A safe copy to perform a transaction retry on.
     */
    public T copyEntityPersist();
}
