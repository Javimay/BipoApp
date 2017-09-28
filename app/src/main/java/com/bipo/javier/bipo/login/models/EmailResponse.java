package com.bipo.javier.bipo.login.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 06/05/2017.
 */

public class EmailResponse implements Serializable {

    @SerializedName("message")
    public String message;

    @SerializedName("error")
    public String error;

    @SerializedName("userExist")
    private ArrayList<User> userExist;

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

    public ArrayList<User> getUserExist() {
        return userExist;
    }

    public void setUserExist(ArrayList<User> userExist) {
        this.userExist = userExist;
    }
}
