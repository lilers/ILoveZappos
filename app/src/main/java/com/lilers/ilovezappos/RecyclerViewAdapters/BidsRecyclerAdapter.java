package com.lilers.ilovezappos.RecyclerViewAdapters;

import android.content.Context;
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

public class BidsRecyclerAdapter extends RecyclerView.Adapter<BidsRecyclerAdapter.BidViewHolder> {
    private List<BidAndAskInfo> bidList;

    public BidsRecyclerAdapter(List<BidAndAskInfo> bids) {
        bidList = new ArrayList<>();
        bidList = bids;
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BidsRecyclerAdapter.BidViewHolder(inflater.inflate(R.layout.bids_recycler_view_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(BidViewHolder holder, int position) {
        BidAndAskInfo bid = bidList.get(position);
        TextView price = holder.price;
        TextView amount = holder.amount;
        TextView value = holder.value;
        double total_value = bid.getAmount() * bid.getPrice();

        amount.setText(String.format("%,.8f", bid.getAmount()));
        price.setText(String.format("$%,.2f", bid.getPrice()));
        value.setText(String.format("$%,.2f", total_value));

        // Change row colors (even vs. odd)
        if(position % 2 == 0) {
            holder.layout.setBackgroundResource(R.color.colorAccent);
        } else {
            holder.layout.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }

    /*
     * Get views to edit
     */
    public class BidViewHolder extends RecyclerView.ViewHolder {
        TextView price, amount, value;
        LinearLayout layout;

        public BidViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.bidPrice);
            amount = (TextView) itemView.findViewById(R.id.bidAmount);
            value = (TextView) itemView.findViewById(R.id.bidValue);
            layout = (LinearLayout) itemView.findViewById(R.id.bidsRecyclerViewRows);
        }
    }
}