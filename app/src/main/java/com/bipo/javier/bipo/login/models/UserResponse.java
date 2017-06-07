package com.bipo.javier.bipo.login.models;

import java.io.Serializable;

/**
 * Created by Javier on 31/05/2017.
 */

public class UserResponse implements Serializable {

    private boolean error;
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
