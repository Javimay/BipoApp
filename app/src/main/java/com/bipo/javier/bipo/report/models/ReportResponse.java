package com.bipo.javier.bipo.report.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Javier on 12/06/2017.
 */

public class ReportResponse implements Serializable{

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("reportId")
    private int reportId;

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

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
