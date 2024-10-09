package fourcorp.buildflow.domain;

public class Client extends Person {
    private ClientType type;

    public Client(String id, int nif, String name, String address, String city, String zipCode, int phoneNumber, ClientType type) {
        super(id, nif, name, address, city, zipCode, phoneNumber);
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }
}
