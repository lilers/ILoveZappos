package com.lilers.ilovezappos.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lily on 8/31/2017.
 */

public class TransactionData {
    // Only got values I needed from API
    @SerializedName("date")
    @Expose
    private float date;

    @SerializedName("price")
    @Expose
    private float price;

    public float getDate() {
        return date;
    }

    public float getPrice() {
        return price;
    }
}