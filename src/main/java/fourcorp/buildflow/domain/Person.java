package fourcorp.buildflow.domain;

/**
 * Represents a person in the system, with an ID, NIF (tax identification number),
 * name, address, and phone number. This is an abstract class meant to be
 * extended by specific types of people, such as employees or clients.
 */
public abstract class Person {
    private String id;
    private int nif;
    private String name;
    private Address address;
    private int phoneNumber;

    /**
     * Constructs a Person with the specified details.
     *
     * @param id          the unique identifier for the person
     * @param nif         the NIF (tax identification number) of the person
     * @param name        the name of the person
     * @param address     the street address of the person
     * @param city        the city where the person is located
     * @param zipCode     the zip code for the person's address
     * @param phoneNumber the phone number of the person
     */
    public Person(String id, int nif, String name, String address, String city, String zipCode, int phoneNumber) {
        this.id = id;
        this.nif = nif;
        this.name = name;
        this.address = new Address(address, city, zipCode);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the unique identifier for the person.
     *
     * @return the ID of the person
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the person.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the NIF (tax identification number) of the person.
     *
     * @return the NIF of the person
     */
    public int getNif() {
        return nif;
    }

    /**
     * Sets the NIF (tax identification number) of the person.
     *
     * @param nif the NIF to set
     */
    public void setNif(int nif) {
        this.nif = nif;
    }

    /**
     * Gets the name of the person.
     *
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the person.
     *
     * @return the Address object representing the person's address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the address of the person using the provided address, city, and zip code.
     *
     * @param address the street address to set
     * @param city    the city to set
     * @param zipCode the zip code to set
     */
    public void setAddress(String address, String city, String zipCode) {
        this.address = new Address(address, city, zipCode);
    }

    /**
     * Gets the phone number of the person.
     *
     * @return the phone number
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the person.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
