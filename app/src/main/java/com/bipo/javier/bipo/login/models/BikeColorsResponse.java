package com.bipo.javier.bipo.login.models;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeColorsResponse {

    public String message;
    public String error;
    public ArrayList<BikeColor> bikeColor;

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

    public ArrayList<BikeColor> getBikeColor() {
        return bikeColor;
    }

    public void setBikeColor(ArrayList<BikeColor> bikeColor) {
        this.bikeColor = bikeColor;
    }
}
