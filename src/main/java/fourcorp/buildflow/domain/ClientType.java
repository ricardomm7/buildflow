package fourcorp.buildflow.domain;

/**
 * Represents the type of client in the system.
 * A client can either be of type PRIVATE or BUSINESS.
 */
public enum ClientType {
    /**
     * Indicates a private individual as a client.
     */
    PRIVATE,

    /**
     * Indicates a business entity as a client.
     */
    BUSINESS
}
