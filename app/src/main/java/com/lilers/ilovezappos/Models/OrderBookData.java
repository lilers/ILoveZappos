package com.lilers.ilovezappos.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lily on 8/31/2017.
 */

public class OrderBookData {
    // Only got values I need from API
    @SerializedName("bids")
    @Expose
    private List<List<String>> bids;

    @SerializedName("asks")
    @Expose
    private List<List<String>> asks;

    public List<List<String>> getBids() {
        return bids;
    }

    public List<List<String>> getAsks() {
        return asks;
    }
}