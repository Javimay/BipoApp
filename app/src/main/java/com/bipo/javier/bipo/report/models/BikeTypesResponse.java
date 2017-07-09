package com.bipo.javier.bipo.report.models;

import com.bipo.javier.bipo.report.models.BikeType;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeTypesResponse {

    public String message;
    public String error;
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
