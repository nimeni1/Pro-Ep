package com.fhict.proep.client.models;

public class Address {

    private String street;
    private int number;
    private String city;
    private String postal_code;

    public Address() { }

    public Address(String street, int number, String city, String postal_code) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal_code = postal_code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
