package com.lilers.ilovezappos;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.lilers.ilovezappos.Fragments.AsksFragment;
import com.lilers.ilovezappos.Fragments.BidsFragment;
import com.lilers.ilovezappos.Fragments.PriceAlertFragment;
import com.lilers.ilovezappos.Fragments.RetainedFragment;
import com.lilers.ilovezappos.Fragments.TransactionFragment;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Comment to self.....something seems wrong here....lots of lines, I probably made it more
 * complicated than I need to.....NEED TO FIX AND WORK ON FRAGMENT IMPLEMENTATIONS
 */
public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private RetainedFragment retainedFragment;
    private Fragment transactionFragment, bidsFragment, asksFragment, priceAlertFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change background of app bar to custom gradient
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background));

        fragmentManager = getSupportFragmentManager();
        retainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.RETAINED_FRAG_TAG);

        // Get retained fragment ready
        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(retainedFragment, RetainedFragment.RETAINED_FRAG_TAG).commit();
        }

        // Transaction fragment is default "home", else find fragments added that were already added
        if (savedInstanceState == null) {
            transactionFragment = new TransactionFragment();
            retainedFragment.setActiveFragTag(TransactionFragment.TRANS_FRAG_TAG);
            fragmentManager.beginTransaction().replace(R.id.frame, transactionFragment).commit();
        } else {
            transactionFragment = fragmentManager.findFragmentByTag(TransactionFragment.TRANS_FRAG_TAG);
            bidsFragment = fragmentManager.findFragmentByTag(BidsFragment.BIDS_FRAG_TAG);
            asksFragment = fragmentManager.findFragmentByTag(AsksFragment.ASKS_FRAG_TAG);
            priceAlertFragment = fragmentManager.findFragmentByTag(PriceAlertFragment.PALERT_FRAG_TAG);
        }

        // Bottom navigation set up, create new fragment if none was created....find old fragment if
        // it was previously created
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.transactionTab:
                        fragment = fragmentManager.findFragmentByTag(TransactionFragment.TRANS_FRAG_TAG);
                        if (fragment == null) {
                            fragment = new TransactionFragment();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        retainedFragment.setActiveFragTag(TransactionFragment.TRANS_FRAG_TAG);
                        return true;
                    case R.id.bidsTab:
                        fragment = fragmentManager.findFragmentByTag(BidsFragment.BIDS_FRAG_TAG);
                        if (fragment == null) {
                            fragment = new BidsFragment();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        retainedFragment.setActiveFragTag(BidsFragment.BIDS_FRAG_TAG);
                        return true;
                    case R.id.asksTab:
                        fragment = fragmentManager.findFragmentByTag(AsksFragment.ASKS_FRAG_TAG);
                        if (fragment == null) {
                            fragment = new AsksFragment();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        retainedFragment.setActiveFragTag(AsksFragment.ASKS_FRAG_TAG);
                        return true;
                    case R.id.priceAlertTab:
                        fragment = fragmentManager.findFragmentByTag(PriceAlertFragment.PALERT_FRAG_TAG);
                        if (fragment == null) {
                            fragment = new PriceAlertFragment();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        retainedFragment.setActiveFragTag(PriceAlertFragment.PALERT_FRAG_TAG);
                        return true;
                    default:
                        return false;
                }
            }
        });

        setAlerter();
    }

    /*
     * Retrofit object created for fragment access
     */
    public static Retrofit retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BitstampAPI.apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /*
     * Set alert details for price alert
     */
    private void setAlerter() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job = dispatcher.newJobBuilder()
                .setService(PriceAlertJobService.class)
                .setTag("PriceAlertJobService")
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(60*60, 60*60+30))
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .build();

        int result = dispatcher.schedule(job);
        if (result != FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS) {
            System.out.println("Scheduling Error");
        }
    }
}