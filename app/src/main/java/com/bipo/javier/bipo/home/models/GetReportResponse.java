package com.bipo.javier.bipo.home.models;

import com.bipo.javier.bipo.report.models.Report;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javier on 25/05/2017.
 */

public class GetReportResponse implements Serializable{

    private String error;
    private String message;
    private ArrayList<Report> reports;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }
}
