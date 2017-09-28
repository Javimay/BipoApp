package com.bipo.javier.bipo.report.models;

import com.bipo.javier.bipo.report.models.BikeType;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeTypesResponse {

    @SerializedName("message")
    public String message;

    @SerializedName("error")
    public String error;

    @SerializedName("biketypes")
    public ArrayList<BikeType> biketypes;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<BikeType> getBiketypes() {
        return biketypes;
    }

    public void setBiketypes(ArrayList<BikeType> biketypes) {
        this.biketypes = biketypes;
    }
}
