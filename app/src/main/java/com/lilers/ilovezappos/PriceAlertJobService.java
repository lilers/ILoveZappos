package com.lilers.ilovezappos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.lilers.ilovezappos.Models.PriceAlertData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Lily on 9/1/2017.
 */

public class PriceAlertJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        // Get stored target price value
        SharedPreferences prefs = getSharedPreferences("targetValue", MODE_PRIVATE);
        final float targetPrice = prefs.getFloat("storedValue", 0);

        Retrofit retrofit = MainActivity.retrofit();
        BitstampAPI bitstampAPI = retrofit.create(BitstampAPI.class);
        Observable<PriceAlertData> priceTrack = bitstampAPI.getTickerHour();
        priceTrack.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PriceAlertData>() {
                    boolean targetReached = false;
                    @Override
                    public void onNext(PriceAlertData data) {
                        float askPrice = Float.parseFloat(data.getAsk());
                        if (askPrice <= targetPrice) {
                            targetReached = true;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Error getting price alert data", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (targetReached == true) {
                            sendAlert();
                        }
                    }
                });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /*
     * Sent an alert when target price is met
     */
    private void sendAlert() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.send_alert_icon)
                        .setContentTitle("BITCOIN PRICE MET!")
                        .setContentText("Click to view")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
        int notificationID = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }
}
