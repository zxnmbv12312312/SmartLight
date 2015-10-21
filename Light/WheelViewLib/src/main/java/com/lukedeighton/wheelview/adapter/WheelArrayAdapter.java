package com.lukedeighton.wheelview.adapter;

import java.util.List;

public abstract class WheelArrayAdapter<T> implements WheelAdapter {
    private List<T> mItems;

    public WheelArrayAdapter(List<T> items) {
        mItems = items;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void setItem(int position,T item) {
        if(position<mItems.size()){
            mItems.set(position,item);
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
