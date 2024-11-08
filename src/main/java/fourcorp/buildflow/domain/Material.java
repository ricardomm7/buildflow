package fourcorp.buildflow.domain;

public class Material {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Material(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

}
