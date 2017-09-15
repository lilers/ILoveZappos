package com.lilers.ilovezappos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lilers.ilovezappos.BitstampAPI;
import com.lilers.ilovezappos.MainActivity;
import com.lilers.ilovezappos.Marker;
import com.lilers.ilovezappos.Models.TransactionData;
import com.lilers.ilovezappos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lily on 8/31/2017.
 */

public class TransactionFragment extends Fragment {
    public static final String TRANS_FRAG_TAG = "TransactionFragment";
    private LineChart lineChart;
    private List<Entry> points;
    private ProgressBar progressBar;

    public TransactionFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        progressBar = view.findViewById(R.id.transactionPBar);
        progressBar.setIndeterminate(true);

        lineChart = view.findViewById(R.id.lineChart);
        lineChart.setVisibility(View.INVISIBLE);

        points = new ArrayList<>();

        getTransactions();

        return view;
    }

    /*
     * Get transactions from API
     */
    private void getTransactions() {
        BitstampAPI bitstampAPI = MainActivity.retrofit().create(BitstampAPI.class);
        Observable<List<TransactionData>> transactions = bitstampAPI.getTransactions();
        transactions.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<TransactionData>>() {
                    @Override
                    public void onNext(@NonNull List<TransactionData> transDataList) {
                        float referenceTime = transDataList.get(transDataList.size() - 1).getDate();
                        for (TransactionData transData: transDataList) {
                            Float time = Float.valueOf(transData.getDate()) - referenceTime;
                            // MARKER NOT WORKING BECAUSE OF DATE VALUE
                            points.add(new Entry(time, transData.getPrice()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity(), "Error getting transactions", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        drawGraph();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /*
     * Set up and populate graph
     */
    private void drawGraph() {
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                return dateFormat.format((long)value * 1000);
            }
        });

        lineChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.format("$%.2f", value);
            }
        });

        LineDataSet lineDataSet = new LineDataSet(points, "Price");
        ArrayList<ILineDataSet> graph = new ArrayList<>();
        lineDataSet.setDrawCircles(false);
        //lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        graph.add(lineDataSet);
        LineData data = new LineData(graph);
        lineChart.setData(data);

        IMarker marker = new Marker(getContext(), R.layout.marker);
        lineChart.setMarker(marker);
        lineChart.invalidate();
    }
}
