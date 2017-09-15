package com.lilers.ilovezappos.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lilers.ilovezappos.R;

/**
 * Created by Lily on 8/31/2017.
 */

public class PriceAlertFragment extends Fragment {
    public static final String PALERT_FRAG_TAG = "PriceAlertFragment";
    private String targetValue;

    public PriceAlertFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_price_alert, container, false);

        final EditText editText = view.findViewById(R.id.priceTarget);
        Button alertButton = view.findViewById(R.id.priceAlertButton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetValue = editText.getText().toString();
                if (targetValue.length() > 0) {
                    storeInput();
                    Toast.makeText(getActivity(), "Price Alert Set!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please enter a value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    /*
     * Store value into preference to retrieve later
     */
    private void storeInput() {
        double target = Double.parseDouble(targetValue);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("targetValue", Context.MODE_PRIVATE).edit();
        editor.putFloat("storedValue", (float)target);
        editor.apply();
    }
}