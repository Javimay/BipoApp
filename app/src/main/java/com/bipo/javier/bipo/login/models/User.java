package com.bipo.javier.bipo.login.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Javier on 15/05/2017.
 */

public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("email")
    private String email;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("cellphone")
    private String cellphone;

    @SerializedName("documentid")
    private String documentid;

    @SerializedName("token")
    private String token;

    @SerializedName("emailReceiver")
    private int emailReceiver;

    @SerializedName("photoPublication")
    private int photoPublication;

    @SerializedName("enableReportUbication")
    private int enableReportUbication;

    @SerializedName("enableLocationUbication")
    private int enableLocationUbication;

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getDocumentid() {
        return documentid;
    }

    public String getToken() {
        return token;
    }

    public int getEmailReceiver() {
        return emailReceiver;
    }

    public int getPhotoPublication() {
        return photoPublication;
    }

    public int getEnableReportUbication() {
        return enableReportUbication;
    }

    public int getEnableLocationUbication() {
        return enableLocationUbication;
    }

    public int getId() {
        return id;
    }
}
