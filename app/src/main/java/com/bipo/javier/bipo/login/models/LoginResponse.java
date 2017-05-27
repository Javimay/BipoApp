package com.bipo.javier.bipo.login.models;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 08/04/2017.
 */

public class LoginResponse implements Serializable {

    private String error;
    private String message;
    private ArrayList<User> user;

    public ArrayList<User> getUser() {
        return user;
    }

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

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }
}

