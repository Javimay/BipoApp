package com.bipo.javier.bipo.login.models;

import java.io.Serializable;

/**
 * Created by Javier on 06/05/2017.
 */

public class EmailResponse implements Serializable {

    public String message;
    public String error;
    public boolean userExist;

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

    public boolean isUserExist() {
        return userExist;
    }

    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }
}
