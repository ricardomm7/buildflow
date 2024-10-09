package fourcorp.buildflow.domain;

public class Address {

    private String address;
    private String city;
    private String zipCode;

    /**
     * Constructs a new Address with the specified address, city, and zip code.
     *
     * @param address the street address.
     * @param city    the city.
     * @param zipCode the zip code, which must be in the format ####-###.
     * @throws IllegalArgumentException if the zip code does not match the required format.
     */
    public Address(String address, String city, String zipCode) {
        setAddress(address);
        setCity(city);
        setZipCode(zipCode);
    }

    /**
     * Sets the zip code of the address.
     *
     * @param zipCode the new zip code, which must be in the format ####-###.
     * @throws IllegalArgumentException if the zip code does not match the required format.
     */
    public void setZipCode(String zipCode) {
        if (verifyZipCode(zipCode)) {
            this.zipCode = zipCode;
        } else {
            throw new IllegalArgumentException("The zip code must be in the format ####-###.");
        }
    }

    /**
     * Sets the street address.
     *
     * @param address the new street address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city of the address.
     *
     * @param city the new city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the street address.
     *
     * @return the street address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the address.
     *
     * @return the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the zip code of the address.
     *
     * @return the zip code.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Verifies if the zip code matches the required format ####-###.
     *
     * @param zipCode the zip code to verify.
     * @return true if the zip code matches the required format, false otherwise.
     */
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
