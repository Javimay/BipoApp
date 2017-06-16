package com.bipo.javier.bipo.home.data;

/**
 * Created by Javier on 15/06/2017.
 */

public class BikeNameFile {

    private String fileName;
    private static BikeNameFile bikeNameFile;

    private BikeNameFile() {

    }

    public static BikeNameFile instance() {
        if(bikeNameFile == null){
            bikeNameFile = new BikeNameFile();
        }
        return bikeNameFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BikeNameFile getBikeNameFile() {
        return bikeNameFile;
    }

}
