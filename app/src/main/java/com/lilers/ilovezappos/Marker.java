package com.lilers.ilovezappos;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Lily on 8/31/2017.
 */

public class Marker extends MarkerView {
    private TextView info;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public Marker(Context context, int layoutResource) {
        super(context, layoutResource);
        info = (TextView) findViewById(R.id.pointInfo);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        info.setText("Time: " + e.getX() + "\nPrice: " + e.getY());
        super.refreshContent(e, highlight);
    }
}
