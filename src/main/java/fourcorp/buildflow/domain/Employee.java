package fourcorp.buildflow.domain;

import java.time.LocalDate;

public class Employee extends Person {
    private LocalDate birthDate;
    private double salary;

    public Employee(String id, int nif, String name, String address, int phoneNumber, LocalDate birthDate, double salary) {
        super(id, nif, name, address, phoneNumber);
        this.birthDate = birthDate;
        this.salary = salary;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
