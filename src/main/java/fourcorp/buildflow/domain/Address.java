package fourcorp.buildflow.domain;

/**
 * Represents an address with a street address, city, and zip code.
 * The zip code must follow the format ####-### where # is a digit.
 * This class provides methods to set and get the address, city, and zip code, while ensuring the zip code format is validated.
 */

public class Address {
    private String address;
    private String city;
    private String zipCode;

    /**
     * Constructs an Address object with the specified address, city, and zip code.
     *
     * @param address the street address
     * @param city the city of the address
     * @param zipCode the zip code, which must be in the format ####-###
     * @throws IllegalArgumentException if the zip code is not in the correct format
     */
    public Address(String address, String city, String zipCode) {
        setAddress(address);
        setCity(city);
        setZipCode(zipCode);
    }

    /**
     * Sets the zip code for this address.
     * The zip code must be in the format ####-### where # is a digit.
     *
     * @param zipCode the zip code to set
     * @throws IllegalArgumentException if the zip code is not in the correct format
     */
    public void setZipCode(String zipCode) {
        if (verifyZipCode(zipCode)) {
            this.zipCode = zipCode;
        } else {
            throw new IllegalArgumentException("The zip code must be in the format ####-###.");
        }
    }

    /**
     * Sets the street address for this address.
     *
     * @param address the street address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city for this address.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the street address.
     *
     * @return the street address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the zip code.
     *
     * @return the zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Verifies if the given zip code is in the format ####-###.
     * The method checks if the length is 8, the fifth character is a hyphen,
     * and all other characters are digits.
     *
     * @param zipCode the zip code to verify
     * @return true if the zip code is in the correct format, false otherwise
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
