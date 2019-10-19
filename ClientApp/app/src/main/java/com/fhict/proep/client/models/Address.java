package com.fhict.proep.client.models;

import java.io.Serializable;

public class Address implements Serializable {

    private String street;
    private String number;
    private String city;
    private String postal_code;
    private double latitude;
    private double longitude;

    public Address(String street, String number, String city, String postal_code, double latitude, double longitude) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal_code = postal_code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
