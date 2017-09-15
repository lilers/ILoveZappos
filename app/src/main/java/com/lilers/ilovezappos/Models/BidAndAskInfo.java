package com.lilers.ilovezappos.Models;

/**
 * Created by Lily on 8/31/2017.
 */

public class BidAndAskInfo {
    // price and amount of bid/ask
    private double price, amount;

    // Constructor to store values for easy access
    public BidAndAskInfo(double price, double amount) {
        this.price = price;
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }
}
