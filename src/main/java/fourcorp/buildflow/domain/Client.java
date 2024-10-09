package fourcorp.buildflow.domain;

import java.time.LocalDate;

public class Client extends Person{
    private ClientType type;

    public Client(String id, int nif, String name, String address, int phoneNumber, ClientType type) {
        super(id, nif, name, address, phoneNumber);
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }
}
