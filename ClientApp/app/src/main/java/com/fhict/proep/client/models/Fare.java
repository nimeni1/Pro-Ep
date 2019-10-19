package com.fhict.proep.client.models;

import java.io.Serializable;

public class Fare implements Serializable {
    private String license_plate;
    private Address start_address;
    private Address destination_address;
    private double distance_km;
    private double total_price;
    private String license_number;
    private DateTime dateTime;
    private String client_email;
    private boolean trip_completed;
    private String driver_email;

    public Fare(DateTime date, String license_number, String license_plate, Address start_address, Address destination_address, double distance_km, double total_price, String client_email, boolean trip_completed, String driver_email) {
        this.license_number = license_number;
        this.start_address = start_address;
        this.destination_address = destination_address;
        this.distance_km = distance_km;
        this.total_price = total_price;
        this.dateTime = date;
        this.license_plate = license_plate;
        this.client_email = client_email;
        this.trip_completed = trip_completed;
        this.driver_email = driver_email;
    }

    public Fare(String client_email, Address start_address, Address destination_address) {
        this.client_email = client_email;
        this.start_address = start_address;
        this.destination_address = destination_address;
    }

    public Fare() {
    }

    public Address getStart_address() {
        return start_address;
    }

    public void setStart_address(Address start_address) {
        this.start_address = start_address;
    }

    public Address getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(Address destination_address) {
        this.destination_address = destination_address;
    }

    public double getDistance_km() {
        return distance_km;
    }

    public void setDistance_km(double distance_km) {
        this.distance_km = distance_km;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public boolean isTrip_completed() {
        return trip_completed;
    }

    public void setTrip_completed(boolean trip_completed) {
        this.trip_completed = trip_completed;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
    }
}