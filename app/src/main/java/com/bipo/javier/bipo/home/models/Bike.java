package com.bipo.javier.bipo.home.models;

import java.io.File;
import java.util.List;

/**
 * Created by Javier on 19/05/2017.
 */

public class Bike {

    private String bikeName;
    private String bikeBrand;
    private String bikeColor;
    private String bikeId;
    private String bikeType;
    private String bikeStatus;
    private String bikeFeatures;
    private List<File> bikePhotos;

    public String getBikeStatus() {
        return bikeStatus;
    }

    public void setBikeStatus(String bikeStatus) {
        this.bikeStatus = bikeStatus;
    }

    public List<File> getBikePhotos() {
        return bikePhotos;
    }

    public void setBikePhotos(List<File> bikePhotos) {
        this.bikePhotos = bikePhotos;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getBikeBrand() {
        return bikeBrand;
    }

    public void setBikeBrand(String bikeBrand) {
        this.bikeBrand = bikeBrand;
    }

    public String getBikeColor() {
        return bikeColor;
    }

    public void setBikeColor(String bikeColor) {
        this.bikeColor = bikeColor;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public String getBikeFeatures() {
        return bikeFeatures;
    }

    public void setBikeFeatures(String bikeFeatures) {
        this.bikeFeatures = bikeFeatures;
    }
}
