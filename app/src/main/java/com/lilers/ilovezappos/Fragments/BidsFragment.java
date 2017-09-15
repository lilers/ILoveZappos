package com.lilers.ilovezappos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lilers.ilovezappos.RecyclerViewAdapters.BidsRecyclerAdapter;
import com.lilers.ilovezappos.BitstampAPI;
import com.lilers.ilovezappos.MainActivity;
import com.lilers.ilovezappos.Models.OrderBookData;
import com.lilers.ilovezappos.Models.BidAndAskInfo;
import com.lilers.ilovezappos.R;

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

public class BidsFragment extends Fragment {
    public static final String BIDS_FRAG_TAG = "BidsFragment";
    private RecyclerView recyclerView;
    private  List<BidAndAskInfo> bidDataList;
    private ProgressBar progressBar;
    private LinearLayout headers;

    public BidsFragment() {
        bidDataList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bids, container, false);

        // Set up loading view
        progressBar = view.findViewById(R.id.bidsPBar);
        progressBar.setIndeterminate(true);
        headers = view.findViewById(R.id.bidsHeaders);
        headers.setVisibility(View.INVISIBLE);

        // Set up recycler view
        recyclerView = view.findViewById(R.id.bidsRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getBids();

        return view;
    }

    /*
     * Get bids from API
     */
    private void getBids() {
        BitstampAPI bitstampAPI = MainActivity.retrofit().create(BitstampAPI.class);
        final Observable<OrderBookData> orderBook = bitstampAPI.getOrderBook();
        orderBook.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OrderBookData>() {
                    @Override
                    public void onNext(@NonNull OrderBookData oBookData) {
                        // Store bid data in list and populate recycler view
                        List<List<String>> bidList = oBookData.getBids();
                        bidDataList = new ArrayList<>();
                        for (List<String> bids: bidList) {
                            BidAndAskInfo bidInfo = new BidAndAskInfo(Double.parseDouble(bids.get(0)), Double.parseDouble(bids.get(1)));
                            bidDataList.add(bidInfo);
                        }
                        recyclerView.setAdapter(new BidsRecyclerAdapter(bidDataList));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity(), "Error getting bids", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        headers.setVisibility(View.VISIBLE);
                    }
                });
    }
}