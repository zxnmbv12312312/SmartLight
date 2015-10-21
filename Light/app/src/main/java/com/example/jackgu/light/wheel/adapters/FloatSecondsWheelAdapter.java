package com.example.jackgu.light.wheel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jackgu.light.R;

/**
 * Created by jackgu on 6/4/2015.
 */
public class FloatSecondsWheelAdapter extends AbstractWheelTextAdapter {


    // Slot machine symbols
    private final int items[] = new int[]{R.string.seconds_1,
            R.string.seconds_2,
            R.string.seconds_3, R.string.seconds_4, R.string.seconds_5, R.string.seconds_6, R.string.seconds_7, R.string.seconds_8, R.string.seconds_9, R.string.seconds_10, R.string.seconds_11,};

    // Layout inflater
    private Context context;

    /**
     * Constructor
     */
    public FloatSecondsWheelAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return  context.getResources().getString(items[index]);
        }
        return null;
    }
}
