package fourcorp.buildflow.domain;

import java.util.Objects;

public class Workstation implements Identifiable<String> {
    private String idMachine;
    private double time;
    private boolean isAvailable;

    public Workstation(String idMachine, double time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
    }

    public String getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(String idMachine) {
        this.idMachine = idMachine;
    }

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void processProduct(Product product) {
        this.setAvailable(false);
        System.out.println("Processing " + product.getId() + " in " + idMachine + " - Estimate " + time + " min");
        this.setAvailable(true);
    }

    @Override
    public String getId() {
        return idMachine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workstation that = (Workstation) o;
        return idMachine.equalsIgnoreCase(that.idMachine);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idMachine);
    }
}
