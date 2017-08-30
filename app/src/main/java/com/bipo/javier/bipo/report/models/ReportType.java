package com.bipo.javier.bipo.report.models;

/**
 * Created by Javier on 11/05/2017.
 */

public class ReportType {

    public int id;
    public String reportType;
    public int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
