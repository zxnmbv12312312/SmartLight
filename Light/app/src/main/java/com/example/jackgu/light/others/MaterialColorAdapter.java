package com.example.jackgu.light.others;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by jackgu on 6/10/2015.
 */
public class MaterialColorAdapter extends WheelArrayAdapter<Map.Entry<String, Integer>> {
    public MaterialColorAdapter(List<Map.Entry<String, Integer>> entries) {
        super(entries);
    }

    @Override
    public Drawable getDrawable(int position) {
        Drawable[] drawable = new Drawable[]{
                createOvalDrawable(getItem(position).getValue())
        };
        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable(int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public Map.Entry<String, Integer> getColor(int position) {
        return getItem(position);
    }
}