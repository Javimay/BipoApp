package com.bipo.javier.bipo.login.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Javier on 31/05/2017.
 */

public class UserResponse implements Serializable {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
