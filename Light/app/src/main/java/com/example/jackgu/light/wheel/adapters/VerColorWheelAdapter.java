package com.example.jackgu.light.wheel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.jackgu.light.R;
import com.example.jackgu.light.others.MaterialColor;

/**
 * Created by jackgu on 5/28/2015.
 */
public class VerColorWheelAdapter extends AbstractWheelAdapter {
    // Image size
    final int IMAGE_WIDTH = 200;
    final int IMAGE_HEIGHT = 50;

    // Slot machine symbols
    private ArrayList<Integer> items = new ArrayList<Integer>();

    // Layout inflater
    private Context context;

    /**
     * Constructor
     */
    public VerColorWheelAdapter(Context context) {
        this.context = context;
        InitItems();
    }

    private void InitItems() {
        List<Map.Entry<String, Integer>> list = MaterialColor.getAllColor(context, "c\\d*_pick$");

        for (Map.Entry<String, Integer> entry : list) {
            items.add(entry.getValue());
        }
    }

    public int getColor(int index) {
        if (index >= 0) {
            return items.get(index);
        } else {
            int index_re = items.size() + index;
            return items.get(index_re);
        }
    }

    public String getColorString(int index) {
        List<Map.Entry<String, Integer>> list = MaterialColor.getAllColor(context, "c\\d*_pick$");
        for (Map.Entry<String, Integer> entry : list) {
            if (index >= 0) {
                if (entry.getValue() == items.get(index)) {
                    return entry.getKey();
                }
            } else {
                int index_re = items.size() + index;
                if (entry.getValue() == items.get(index_re)) {
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    /**
     * Loads image from resources
     */
    private Bitmap loadImage(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
        bitmap.recycle();
        return scaled;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    // Layout params for image view
    final LayoutParams params = new LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        ImageView img;
        if (cachedView != null) {
            img = (ImageView) cachedView;
        } else {
            img = new ImageView(context);
        }
        img.setLayoutParams(params);
        //ColorDrawable bitmapRef = images.get(index);
        //Bitmap bitmap = bitmapRef.get();
        if (index >= items.size()) {
            //bitmap = loadImage(items[index]);
            //images.set(index, bitmapRef);
        } else {
            //img.setImageDrawable(bitmapRef);
            img.setBackgroundColor(items.get(index));
        }
        return img;
    }
}
