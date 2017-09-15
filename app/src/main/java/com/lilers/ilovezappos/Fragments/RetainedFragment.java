package com.lilers.ilovezappos.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Lily on 8/31/2017.
 */

public class RetainedFragment extends Fragment {
    public static final String RETAINED_FRAG_TAG = "RetainedFragment";
    private String activeFragTag;

    public RetainedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public String getActiveFragTag() {
        return activeFragTag;
    }

    public void setActiveFragTag(String tag) {
        activeFragTag = tag;
    }
}
