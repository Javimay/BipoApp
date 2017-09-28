package com.bipo.javier.bipo.report.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeColor {

    @SerializedName("id")
    public int id;

    @SerializedName("color")
    public String color;

    @SerializedName("hexColor")
    public String hexColor;

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }
}
