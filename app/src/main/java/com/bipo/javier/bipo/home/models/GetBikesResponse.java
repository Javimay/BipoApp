package com.bipo.javier.bipo.home.models;

import com.bipo.javier.bipo.account.models.Bike;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 12/06/2017.
 */

public class GetBikesResponse implements Serializable {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("bikes")
    private ArrayList<Bike> bikes;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(ArrayList<Bike> bikes) {
        this.bikes = bikes;
    }
}
