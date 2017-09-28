package com.bipo.javier.bipo.account.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Javier on 21/07/2017.
 */

public class BikePhotos {

    @SerializedName("url")
    private String url;

    @SerializedName("thumbUrl")
    private String thumbUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
