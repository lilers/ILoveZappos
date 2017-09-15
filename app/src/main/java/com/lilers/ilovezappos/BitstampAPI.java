package com.lilers.ilovezappos;

import com.lilers.ilovezappos.Models.OrderBookData;
import com.lilers.ilovezappos.Models.PriceAlertData;
import com.lilers.ilovezappos.Models.TransactionData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Lily on 8/31/2017.
 */

public interface BitstampAPI {
    String apiBaseURL = "https://www.bitstamp.net/api/v2/";

    @GET("transactions/btcusd/")
    Observable<List<TransactionData>> getTransactions();

    @GET("order_book/btcusd/")
    Observable<OrderBookData> getOrderBook();

    @GET("ticker_hour/btcusd")
    Observable<PriceAlertData> getTickerHour();
}
