package com.bipo.javier.bipo.account.models;

import java.io.File;
import java.util.List;

/**
 * Created by Javier on 19/05/2017.
 */

public class Bike {

    private int id;
    private String bikename;
    private String brand;
    private String color;
    private String hexcolor;
    private String idframe;
    private String type;
    private String bikefeatures;
    private String bikestate;
    private boolean defaultbike = false;
    private boolean checked = false;
    //TODO:Poner tipo de foto de la bicicleta
    // private List<File> bikePhotos;


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

    public boolean isDefaultbike() {
        return defaultbike;
    }

    public void setDefaultbike(boolean defaultbike) {
        this.defaultbike = defaultbike;
    }
}
