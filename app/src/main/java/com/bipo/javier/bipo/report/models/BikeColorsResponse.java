package com.bipo.javier.bipo.report.models;

import com.bipo.javier.bipo.report.models.BikeColor;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeColorsResponse {

    @SerializedName("message")
    public String message;

    @SerializedName("error")
    public String error;

    @SerializedName("bikeColor")
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
