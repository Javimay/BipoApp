package com.bipo.javier.bipo.report.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 11/05/2017.
 */

public class ReportTypesResponse implements Serializable{

    public String message;
    public String error;
    public ArrayList<ReportType> brands;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setBrands(ArrayList<ReportType> bikeStates) {
        this.brands = bikeStates;
    }

    public ArrayList<ReportType> getBrands() {
        return brands;
    }

}
