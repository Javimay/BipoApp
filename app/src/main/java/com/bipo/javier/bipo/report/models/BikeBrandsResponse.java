package com.bipo.javier.bipo.report.models;

import com.bipo.javier.bipo.report.models.BikeBrand;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeBrandsResponse {

    @SerializedName("message")
    public String message;

    @SerializedName("error")
    public String error;

    @SerializedName("brands")
    public ArrayList<BikeBrand> brands;

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

    public ArrayList<BikeBrand> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<BikeBrand> brands) {
        this.brands = brands;
    }
}
