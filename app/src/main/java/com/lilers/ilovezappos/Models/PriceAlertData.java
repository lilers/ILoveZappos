package com.lilers.ilovezappos.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lily on 9/1/2017.
 */

public class PriceAlertData {
    // Only got values I need from API
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("ask")
    @Expose
    private String ask;

    public String getAsk() {
        return ask;
    }
}