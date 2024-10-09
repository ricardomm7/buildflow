package fourcorp.buildflow.domain;

import java.time.LocalDate;

public abstract class Person {
    private String id;
    private int nif;
    private String name;
    private String address;
    private int phoneNumber;

    public Person(String id, int nif, String name, String address, int phoneNumber) {
        this.id = id;
        this.nif = nif;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



}
