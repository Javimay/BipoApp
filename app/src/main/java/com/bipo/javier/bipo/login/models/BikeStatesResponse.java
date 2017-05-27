package com.bipo.javier.bipo.login.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 11/05/2017.
 */

public class BikeStatesResponse implements Serializable{

    public String message;
    public String error;
    public ArrayList<BikeState> bikeStates;

    public ArrayList<BikeState> getBikeStates() {
        return bikeStates;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setBikeStates(ArrayList<BikeState> bikeStates) {
        this.bikeStates = bikeStates;
    }
}
