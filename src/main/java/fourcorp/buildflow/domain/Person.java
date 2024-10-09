package fourcorp.buildflow.domain;

public abstract class Person {
    private String id;
    private int nif;
    private String name;
    private Address address;
    private int phoneNumber;

    public Person(String id, int nif, String name, String address, String city, String zipCode, int phoneNumber) {
        this.id = id;
        this.nif = nif;
        this.name = name;
        this.address = new Address(address, city, zipCode);
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(String address, String city, String zipCode) {
        this.address = new Address(address, city, zipCode);
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
