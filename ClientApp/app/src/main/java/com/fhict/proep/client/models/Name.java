package com.fhict.proep.client.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Name implements Serializable {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
