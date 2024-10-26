package fourcorp.buildflow.domain;

/**
 * Represents an entity that can be identified by a unique identifier.
 *
 * @param <ID> the type of the identifier
 */
public interface Identifiable<ID> {

    /**
     * Gets the unique identifier for the entity.
     *
     * @return the identifier of type ID
     */
    ID getId();
}

