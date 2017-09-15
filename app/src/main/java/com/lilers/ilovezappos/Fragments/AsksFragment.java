package com.lilers.ilovezappos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lilers.ilovezappos.BitstampAPI;
import com.lilers.ilovezappos.MainActivity;
import com.lilers.ilovezappos.Models.BidAndAskInfo;
import com.lilers.ilovezappos.Models.OrderBookData;
import com.lilers.ilovezappos.R;
import com.lilers.ilovezappos.RecyclerViewAdapters.AsksRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lily on 8/31/2017.
 */

public class AsksFragment extends Fragment {
    public static final String ASKS_FRAG_TAG ="AsksFragment";
    private RecyclerView recyclerView;
    private List<BidAndAskInfo> askDataList;
    private ProgressBar progressBar;
    private LinearLayout headers;

    public AsksFragment() {
        askDataList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asks, container, false);

        // Set up loading view
        progressBar = view.findViewById(R.id.asksPBar);
        progressBar.setIndeterminate(true);
        headers = view.findViewById(R.id.asksHeaders);
        headers.setVisibility(View.INVISIBLE);

        // Set up recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.asksRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getAsks();

        return view;
    }

    /*
     * Get asks from API
     */
    private void getAsks() {
        BitstampAPI bitstampAPI = MainActivity.retrofit().create(BitstampAPI.class);
        final Observable<OrderBookData> orderBook = bitstampAPI.getOrderBook();
        orderBook.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OrderBookData>() {
                    @Override
                    public void onNext(@NonNull OrderBookData oBookData) {
                        // Store ask data in list and populate recycler view
                        List<List<String>> askList = oBookData.getAsks();
                        askDataList = new ArrayList<>();
                        for (List<String> asks: askList) {
                            BidAndAskInfo askInfo = new BidAndAskInfo(Double.parseDouble(asks.get(0)), Double.parseDouble(asks.get(1)));
                            askDataList.add(askInfo);
                        }
                        recyclerView.setAdapter(new AsksRecyclerAdapter(askDataList));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity(), "Error getting asks", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        headers.setVisibility(View.VISIBLE);
                    }
                });
    }
}