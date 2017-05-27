package com.bipo.javier.bipo.login.models;

import java.util.ArrayList;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeBrandsResponse {

    public String message;
    public String error;
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
