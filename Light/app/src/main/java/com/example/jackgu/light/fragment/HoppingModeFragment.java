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

public class HoppingModeFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private CircleImageView imageView1;
    private CircleImageView imageView2;
    private CircleImageView imageView3;
    private CircleImageView imageView4;
    private CircleImageView imageView5;
    private CircleImageView imageView6;
    private CircleImageView imageView7;
    private TitleView titleView;

    private ImageButton favButton;
    private TextView lastFavIdText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hopping_mode, container, false);
        mActivity = getActivity();
        mParent = view;
        titleView = (TitleView) view.findViewById(R.id.hopping_mode_title);
        titleView.setTitle(R.string.hopping_mode_title);

        imageView1 = (CircleImageView)view.findViewById(R.id.image_color1);
        imageView2 = (CircleImageView)view.findViewById(R.id.image_color2);
        imageView3 = (CircleImageView)view.findViewById(R.id.image_color3);
        imageView4 = (CircleImageView)view.findViewById(R.id.image_color4);
        imageView5 = (CircleImageView)view.findViewById(R.id.image_color5);
        imageView6 = (CircleImageView)view.findViewById(R.id.image_color6);
        imageView7 = (CircleImageView)view.findViewById(R.id.image_color7);
        favButton = (ImageButton)view.findViewById(R.id.imageFavButton);
        lastFavIdText = (TextView) view.findViewById(R.id.last_favor_id);
        RenderView(view);
        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(favButton.getTag().equals("favor_btn_off")) {
                    Favorite();
                }else {
                    DisableFavorite();
                }
            }
        });
        return view;
    }

    public void RenderView(View view){
        initSecondsWheel(view, R.id.time_vertical_wheel1);
        ModeModel modeModel=FileHelper.GetModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Hopping");
        if(modeModel!=null) {
            WheelView timewheel1 = (WheelView) view.findViewById(R.id.time_vertical_wheel1);
            timewheel1.setCurrentItem(modeModel.getSecondsWheelNumber().get(0));
            if (modeModel.getIsFavorite()){
                favButton.setImageResource(R.drawable.favor_btn_on);
                favButton.setTag("favor_btn_on");
            }else{
                favButton.setImageResource(R.drawable.favor_btn_off);
                favButton.setTag("favor_btn_off");
            }
        }
    }

    public void Favorite(){
        favButton.setImageResource(R.drawable.favor_btn_on);
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOn();

        String color1 = (String)imageView1.getTag();
        String color2 = (String)imageView2.getTag();
        String color3 = (String)imageView3.getTag();
        String color4 = (String)imageView4.getTag();
        String color5 = (String)imageView5.getTag();
        String color6 = (String)imageView6.getTag();
        String color7 = (String)imageView7.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.addColor(color4);
        modeModel.addColor(color5);
        modeModel.addColor(color6);
        modeModel.addColor(color7);
        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        int index1 = wheel1.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        modeModel.addSecond(second1.toString());
        adapter1 = null;
        modeModel.setMode("Hopping");
        int no = FileHelper.AddMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(no + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Hopping", true, no);
    }

    public void DisableFavorite(){
        favButton.setImageResource(R.drawable.favor_btn_off);
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOff();

        String color1 = (String)imageView1.getTag();
        String color2 = (String)imageView2.getTag();
        String color3 = (String)imageView3.getTag();
        String color4 = (String)imageView4.getTag();
        String color5 = (String)imageView5.getTag();
        String color6 = (String)imageView6.getTag();
        String color7 = (String)imageView7.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.addColor(color4);
        modeModel.addColor(color5);
        modeModel.addColor(color6);
        modeModel.addColor(color7);
        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        int index1 = wheel1.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        modeModel.addSecond(second1.toString());
        adapter1 = null;
        modeModel.setMode("Hopping");
        FileHelper.RemoveMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(-1 + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Hopping", false, -1);
    }

    private void initSecondsWheel(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        wheel.setViewAdapter(new FloatSecondsWheelAdapter(getActivity()));
        wheel.setCurrentItem(1);
        wheel.setWheelBackground(R.color.grey_600);
        wheel.setDrawShadows(false);
        wheel.addChangingListener(changedSecondsListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    // Wheel changed listener
    private OnWheelChangedListener changedSecondsListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            favButton.setImageResource(R.drawable.favor_btn_off);
            favButton.setTag("favor_btn_off");
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            String color1 = (String)imageView1.getTag();
            String color2 = (String)imageView2.getTag();
            String color3 = (String)imageView3.getTag();
            String color4 = (String)imageView4.getTag();
            String color5 = (String)imageView5.getTag();
            String color6 = (String)imageView6.getTag();
            String color7 = (String)imageView7.getTag();
            ModeModel modeModel = new ModeModel();
            modeModel.addColor(color1);
            modeModel.addColor(color2);
            modeModel.addColor(color3);
            modeModel.addColor(color4);
            modeModel.addColor(color5);
            modeModel.addColor(color6);
            modeModel.addColor(color7);
            WheelView wheel1 = (WheelView)mParent.findViewById(R.id.time_vertical_wheel1);
            int index1 = wheel1.getCurrentItem();
            FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
            CharSequence second1 = adapter1.getItemText(index1);
            modeModel.addSecond(second1.toString(),index1);
            adapter1 = null;
            modeModel.setMode("Hopping");
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
