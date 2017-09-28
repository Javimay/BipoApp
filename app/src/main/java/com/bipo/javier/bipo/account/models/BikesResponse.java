package com.bipo.javier.bipo.account.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Javier on 12/06/2017.
 */

public class BikesResponse implements Serializable{

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

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
}
