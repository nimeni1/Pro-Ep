package com.fhict.proep.client.models;

import com.google.gson.annotations.SerializedName;
import com.fhict.proep.client.models.Name;

import java.io.Serializable;

public class Client implements Serializable {
    @SerializedName("_id")
    private ObjectId id;
    private Name name;
    private String email;
    private String password;
    @SerializedName("phone_number")
    private String phone;

    public Client(Name name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }



    public ObjectId getObjectId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private class ObjectId implements Serializable {
        @SerializedName("$oid")
        String actualId;

        public String getActualId() {
            return actualId;
        }
    }
}
