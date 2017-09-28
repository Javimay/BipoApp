package com.bipo.javier.bipo.account.models;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * Created by Javier on 19/05/2017.
 */

public class Bike {

    @SerializedName("id")
    private int id;

    @SerializedName("bikename")
    private String bikename;

    @SerializedName("brand")
    private String brand;

    @SerializedName("color")
    private String color;

    @SerializedName("hexcolor")
    private String hexcolor;

    @SerializedName("idframe")
    private String idframe;

    @SerializedName("type")
    private String type;

    @SerializedName("bikefeatures")
    private String bikefeatures;

    @SerializedName("bikestate")
    private String bikestate;

    @SerializedName("isDefault")
    private int isDefault;

    @SerializedName("checked")
    private boolean checked = false;

    @SerializedName("bikePhotos")
    private List<BikePhotos> bikePhotos;

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBikename() {
        return bikename;
    }

    public void setBikename(String bikename) {
        this.bikename = bikename;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHexcolor() {
        return hexcolor;
    }

    public void setHexcolor(String hexcolor) {
        this.hexcolor = hexcolor;
    }

    public String getIdframe() {
        return idframe;
    }

    public void setIdframe(String idframe) {
        this.idframe = idframe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBikefeatures() {
        return bikefeatures;
    }

    public void setBikefeatures(String bikefeatures) {
        this.bikefeatures = bikefeatures;
    }

    public String getBikestate() {
        return bikestate;
    }

    public void setBikestate(String bikestate) {
        this.bikestate = bikestate;
    }

    public List<BikePhotos> getBikePhotos() {
        return bikePhotos;
    }

    public void setBikePhotos(List<BikePhotos> bikePhotos) {
        this.bikePhotos = bikePhotos;
    }
}
