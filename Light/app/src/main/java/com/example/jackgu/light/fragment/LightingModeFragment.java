package com.example.jackgu.light.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.example.jackgu.light.HelpActivity;
import com.example.jackgu.light.R;
import com.example.jackgu.light.model.ModeModel;
import com.example.jackgu.light.others.FileHelper;
import com.example.jackgu.light.view.TitleView;
import com.example.jackgu.light.wheel.OnWheelChangedListener;
import com.example.jackgu.light.wheel.OnWheelScrollListener;
import com.example.jackgu.light.wheel.WheelView;
import com.example.jackgu.light.wheel.adapters.FloatSecondsWheelAdapter;
import com.example.jackgu.light.wheel.adapters.NumericWheelAdapter;
import com.example.jackgu.light.wheel.adapters.VerColorWheelAdapter;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
//import com.example.jackgu.light.view.TitleView;
//import com.example.jackgu.light.view.TitleView.OnLeftButtonClickListener;
//import com.example.jackgu.light.view.TitleView.OnRightButtonClickListener;


public class LightingModeFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private CircleImageView imageView1;
    private CircleImageView imageView2;
    private CircleImageView imageView3;
    private CircleImageView imageView4;

    private TitleView titleView;

    private ImageButton favButton;
    private TextView lastFavIdText;
    private String filePath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lighting_mode, container, false);
        filePath = getActivity().getFilesDir().getPath().toString();
        mParent = view;
        titleView = (TitleView) view.findViewById(R.id.lighting_mode_title);
        titleView.setTitle(R.string.lighting_mode_title);
        titleView.setTitleImage(R.drawable.lighting_on);
        imageView1 = (CircleImageView) view.findViewById(R.id.image_color1);
        imageView2 = (CircleImageView) view.findViewById(R.id.image_color2);
        imageView3 = (CircleImageView) view.findViewById(R.id.image_color3);
        imageView4 = (CircleImageView) view.findViewById(R.id.image_color4);
        favButton = (ImageButton) view.findViewById(R.id.imageFavButton);
        lastFavIdText = (TextView) view.findViewById(R.id.last_favor_id);
        RenderView(view);

        favButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (favButton.getTag().equals("favor_btn_off")) {
                    Favorite();
                } else {
                    DisableFavorite();
                }
            }
        });
        return view;
    }

    private void RenderView(View view){

        ModeModel modeModel=FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Lighting");
        initWheel(view, R.id.color_vertical_wheel1);
        initWheel(view, R.id.color_vertical_wheel2);
        initWheel(view, R.id.color_vertical_wheel3);
        initWheel(view, R.id.color_vertical_wheel4);
        if(modeModel!=null) {
            WheelView wheel1 = (WheelView) view.findViewById(R.id.color_vertical_wheel1);
            wheel1.setCurrentItem(modeModel.getColorsWheelNumber().get(0));
            WheelView wheel2 = (WheelView) view.findViewById(R.id.color_vertical_wheel2);
            wheel2.setCurrentItem(modeModel.getColorsWheelNumber().get(1));
            WheelView wheel3 = (WheelView) view.findViewById(R.id.color_vertical_wheel3);
            wheel3.setCurrentItem(modeModel.getColorsWheelNumber().get(2));
            WheelView wheel4 = (WheelView) view.findViewById(R.id.color_vertical_wheel4);
            wheel4.setCurrentItem(modeModel.getColorsWheelNumber().get(3));
            int colorId1 = getResources().getIdentifier(modeModel.getColors().get(0), "color", "com.example.jackgu.light");
            imageView1.setImageResource(colorId1);
            imageView1.setTag(modeModel.getColors().get(0));
            int colorId2 = getResources().getIdentifier(modeModel.getColors().get(1), "color", "com.example.jackgu.light");
            imageView2.setImageResource(colorId2);
            imageView2.setTag(modeModel.getColors().get(1));
            int colorId3 = getResources().getIdentifier(modeModel.getColors().get(2), "color", "com.example.jackgu.light");
            imageView3.setImageResource(colorId3);
            imageView3.setTag(modeModel.getColors().get(2));
            int colorId4 = getResources().getIdentifier(modeModel.getColors().get(3), "color", "com.example.jackgu.light");
            imageView4.setImageResource(colorId4);
            imageView4.setTag(modeModel.getColors().get(3));

            if (modeModel.getIsFavorite()){
                favButton.setImageResource(R.drawable.favor_btn_on);
                favButton.setTag("favor_btn_on");
            }else{
                favButton.setImageResource(R.drawable.favor_btn_off);
                favButton.setTag("favor_btn_off");
            }
        }
    }

    public void Favorite() {
        favButton.setImageResource(R.drawable.favor_btn_on);
        favButton.setTag("favor_btn_on");
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOn();

        String color1 = (String) imageView1.getTag();
        String color2 = (String) imageView2.getTag();
        String color3 = (String) imageView3.getTag();
        String color4 = (String) imageView4.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.addColor(color4);
        modeModel.addSecond("0.5");
        modeModel.addSecond("0.5");
        modeModel.addSecond("0.5");
        modeModel.addSecond("3");
        modeModel.setMode("Lighting");
        int no = FileHelper.AddMode(filePath + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(no + "");
        FileHelper.FavorModeSetting(filePath + "/mode_setting.json", "Lighting", true, no);
    }

    public void DisableFavorite() {
        favButton.setImageResource(R.drawable.favor_btn_off);
        favButton.setTag("favor_btn_off");
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOff();

        String color1 = (String) imageView1.getTag();
        String color2 = (String) imageView2.getTag();
        String color3 = (String) imageView3.getTag();
        String color4 = (String) imageView4.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.addColor(color4);
        modeModel.addSecond("0.5");
        modeModel.addSecond("0.5");
        modeModel.addSecond("0.5");
        modeModel.addSecond("3");
        modeModel.setMode("Lighting");
        FileHelper.RemoveMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(-1 + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Lighting", false, -1);
    }

    private void initWheel(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        wheel.setViewAdapter(new VerColorWheelAdapter(getActivity()));
        wheel.setCurrentItem((int) (5));
        wheel.setWheelBackground(R.color.grey_600);
        wheel.setDrawShadows(false);
        wheel.setWheelForeground(R.color.color_transparent);
        wheel.setVisibleItems(10);
        //wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private int getCurrentWheelIndex(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        return wheel.getCurrentItem();
    }

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int id = wheel.getId();
            if (id == R.id.color_vertical_wheel1) {
                VerColorWheelAdapter adapter = (VerColorWheelAdapter) wheel.getViewAdapter();
                int firstItem = wheel.getFirstItem();
                int color = adapter.getColor(firstItem);
                imageView1.setBackgroundColor(color);
            }
        }
    };

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            favButton.setImageResource(R.drawable.favor_btn_off);
            favButton.setTag("favor_btn_off");
            VerColorWheelAdapter adapter = (VerColorWheelAdapter) wheel.getViewAdapter();
            int firstItem = wheel.getFirstItem();
            String colorstr = adapter.getColorString(firstItem);
            int resourceId = getResources().getIdentifier(colorstr, "color", "com.example.jackgu.light");
            int id = wheel.getId();

            if (id == R.id.color_vertical_wheel1) {
                imageView1.setImageResource(resourceId);
                imageView1.setTag(colorstr);
            }
            if (id == R.id.color_vertical_wheel2) {
                imageView2.setImageResource(resourceId);
                imageView2.setTag(colorstr);
            }
            if (id == R.id.color_vertical_wheel3) {
                imageView3.setImageResource(resourceId);
                imageView3.setTag(colorstr);
            }
            if (id == R.id.color_vertical_wheel4) {
                imageView4.setImageResource(resourceId);
                imageView4.setTag(colorstr);
            }
            adapter = null;
        }
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mParent = getView();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            String color1 = (String) imageView1.getTag();
            String color2 = (String) imageView2.getTag();
            String color3 = (String) imageView3.getTag();
            String color4 = (String) imageView4.getTag();
            int index1 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel1);
            int index2 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel2);
            int index3 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel3);
            int index4 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel4);

            ModeModel modeModel = new ModeModel();
            modeModel.addColor(color1,index1);
            modeModel.addColor(color2,index2);
            modeModel.addColor(color3,index3);
            modeModel.addColor(color4,index4);
            modeModel.addSecond("0.5");
            modeModel.addSecond("0.5");
            modeModel.addSecond("0.5");
            modeModel.addSecond("3");
            modeModel.setMode("Lighting");
            modeModel.setIsFavorite(favButton.getTag().equals("favor_btn_on"));
            modeModel.setNo(Integer.parseInt(lastFavIdText.getText().toString()));
            FileHelper.AddModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", modeModel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
