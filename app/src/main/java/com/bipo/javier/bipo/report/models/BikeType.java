package com.bipo.javier.bipo.report.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeType {

    @SerializedName("id")
    public int id;

    @SerializedName("type")
    public String type;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
