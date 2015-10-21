package com.example.jackgu.light.others;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by jackgu on 5/29/2015.
 */
public class CircleIconAdapter extends WheelArrayAdapter<Integer> {

    // Image size
    final int IMAGE_WIDTH = 60;
    final int IMAGE_HEIGHT = 36;
    Activity mActivity = null;

    public CircleIconAdapter(Activity activity, List<Integer> entries) {
        super(entries);
        mActivity = activity;
    }

    @Override
    public Drawable getDrawable(int position) {
        int icon_id = getItem(position);
        return mActivity.getResources().getDrawable(icon_id);
    }
}
