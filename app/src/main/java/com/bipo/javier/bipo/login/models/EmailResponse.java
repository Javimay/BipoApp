package com.bipo.javier.bipo.login.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 06/05/2017.
 */

public class EmailResponse implements Serializable {

    public String message;
    public String error;
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
}
