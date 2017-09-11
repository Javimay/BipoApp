package com.bipo.javier.bipo.login.models;

/**
 * Created by Javier on 15/05/2017.
 */

public class User {

    private int id;
    private String name;
    private String lastname;
    private String nickname;
    private String email;
    private String birthdate;
    private String cellphone;
    private String documentid;
    private String token;
    private int emailReceiver;
    private int photoPublication;
    private int enableReportUbication;
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
