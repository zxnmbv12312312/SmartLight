package com.example.jackgu.light.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jackgu.light.R;
import com.example.jackgu.light.view.TitleView;


public class ResetPasswordFragment extends Fragment {
    private View mParent;

    private TitleView mTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mTitle = (TitleView) view.findViewById(R.id.title_view);
        mTitle.setTitle(R.string.setting_password);
        mTitle.setRightButton(R.string.save, new TitleView.OnRightButtonClickListener(){

            @Override
            public void onClick(View button) {
                //send reset command
            }

        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
