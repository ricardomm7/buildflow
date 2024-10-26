package fourcorp.buildflow.domain;

/**
 * Represents a client who is a type of person in the system.
 * In addition to the attributes inherited from the Person class, the Client class has a specific client type.
 */
public class Client extends Person {
    private ClientType type;

    /**
     * Constructs a Client object with the specified details.
     *
     * @param id          the unique identifier for the client
     * @param nif         the NIF (tax identification number) of the client
     * @param name        the name of the client
     * @param address     the street address of the client
     * @param city        the city where the client is located
     * @param zipCode     the zip code for the client's address
     * @param phoneNumber the phone number of the client
     * @param type        the type of the client (e.g., individual, company)
     */
    public Client(String id, int nif, String name, String address, String city, String zipCode, int phoneNumber, ClientType type) {
        super(id, nif, name, address, city, zipCode, phoneNumber);
        this.type = type;
    }

    /**
     * Gets the type of the client.
     *
     * @return the client type
     */
    public ClientType getType() {
        return type;
    }

    /**
     * Sets the type of the client.
     *
     * @param type the client type to set
     */
    public void setType(ClientType type) {
        this.type = type;
    }
}
