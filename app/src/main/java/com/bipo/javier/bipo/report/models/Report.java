package com.bipo.javier.bipo.report.models;

import java.util.List;

/**
 * Created by Javier on 22/05/2017.
 */

public class Report {

    private String id;
    private String reportName;
    private String report_owner;
    private int idreportType;
    private String reportType;
    private String fhReport;
    private String googlemapscoordinate;
    private int idBike;
    private String bikeName;
    private String color;
    private String brand;
    private String type;
    private String bike_owner;
    private String reportDetails;
    private String fhUpdated;
    //private List<String> reportPhotos;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReport_owner() {
        return report_owner;
    }

    public void setReport_owner(String report_owner) {
        this.report_owner = report_owner;
    }

    public int getIdreportType() {
        return idreportType;
    }

    public void setIdreportType(int idreportType) {
        this.idreportType = idreportType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getFhReport() {
        return fhReport;
    }

    public void setFhReport(String fhReport) {
        this.fhReport = fhReport;
    }

    public String getGooglemapscoordinate() {
        return googlemapscoordinate;
    }

    public void setGooglemapscoordinate(String googlemapscoordinate) {
        this.googlemapscoordinate = googlemapscoordinate;
    }

    public int getIdBike() {
        return idBike;
    }

    public void setIdBike(int idBike) {
        this.idBike = idBike;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getBike_owner() {
        return bike_owner;
    }

    public void setBike_owner(String bike_owner) {
        this.bike_owner = bike_owner;
    }

    public String getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(String reportDetails) {
        this.reportDetails = reportDetails;
    }

    public String getFhUpdated() {
        return fhUpdated;
    }

    public void setFhUpdated(String fhUpdated) {
        this.fhUpdated = fhUpdated;
    }

    /*public List<String> getReportPhotos() {
        return reportPhotos;
    }

    public void setReportPhotos(List<String> reportPhotos) {
        this.reportPhotos = reportPhotos;
    }*/
}