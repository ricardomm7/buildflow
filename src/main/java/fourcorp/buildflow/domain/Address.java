package fourcorp.buildflow.domain;

public class Address {
    private String address;
    private String city;
    private String zipCode;

    public Address(String address, String city, String zipCode) {
        setAddress(address);
        setCity(city);
        setZipCode(zipCode);
    }

    public void setZipCode(String zipCode) {
        if (verifyZipCode(zipCode)) {
            this.zipCode = zipCode;
        } else {
            throw new IllegalArgumentException("The zip code must be in the format ####-###.");
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    private boolean verifyZipCode(String zipCode) {
        if (zipCode == null || zipCode.length() != 8 || zipCode.charAt(4) != '-') {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            if (i != 4 && !Character.isDigit(zipCode.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
