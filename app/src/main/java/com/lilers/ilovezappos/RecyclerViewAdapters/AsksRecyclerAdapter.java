package com.lilers.ilovezappos.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilers.ilovezappos.Models.BidAndAskInfo;
import com.lilers.ilovezappos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 9/1/2017.
 */

public class AsksRecyclerAdapter extends RecyclerView.Adapter<AsksRecyclerAdapter.AskViewHolder> {
    private List<BidAndAskInfo> askList;

    public AsksRecyclerAdapter(List<BidAndAskInfo> asks) {
        askList = new ArrayList<>();
        askList = asks;
    }

    @Override
    public AskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new AsksRecyclerAdapter.AskViewHolder(inflater.inflate(R.layout.asks_recycler_view_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(AskViewHolder holder, int position) {
        BidAndAskInfo ask = askList.get(position);
        TextView price = holder.price;
        TextView amount = holder.amount;
        TextView value = holder.value;
        double total_value = ask.getAmount() * ask.getPrice();

        amount.setText(String.format("%,.8f", ask.getAmount()));
        price.setText(String.format("$%,.2f", ask.getPrice()));
        value.setText(String.format("$%,.2f", total_value));

        // Change row colors (even vs. odd)
        if(position % 2 == 0) {
            holder.layout.setBackgroundResource(R.color.colorPrimary);
        } else {
            holder.layout.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return askList.size();
    }

    /*
     * Get views to edit
     */
    public class AskViewHolder extends RecyclerView.ViewHolder {
        TextView price, amount, value;
        LinearLayout layout;

        public AskViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.askPrice);
            amount = (TextView) itemView.findViewById(R.id.askAmount);
            value = (TextView) itemView.findViewById(R.id.askValue);
            layout = (LinearLayout) itemView.findViewById(R.id.asksRecyclerViewRows);
        }
    }
}