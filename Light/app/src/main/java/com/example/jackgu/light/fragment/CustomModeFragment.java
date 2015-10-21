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


public class CustomModeFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private CircleImageView imageView1;

    private CircleImageView imageView2;

    private CircleImageView imageView3;

    private TitleView titleView;

    private ImageButton favButton;

    private TextView lastFavIdText;
    /**
     * Create a new instance of DetailsFragment, initialized to show the text at
     * 'index'.
     */
    public static CustomModeFragment newInstance(int index) {
        CustomModeFragment f = new CustomModeFragment();

        // Supply index input as an argument.  
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_mode, container, false);
        mParent = view;
        titleView = (TitleView) view.findViewById(R.id.custom_mode_title);
        titleView.setTitle(R.string.custom_mode_title);
        imageView1 = (CircleImageView)view.findViewById(R.id.image_color1);
        imageView2 = (CircleImageView)view.findViewById(R.id.image_color2);
        imageView3 = (CircleImageView)view.findViewById(R.id.image_color3);
        favButton = (ImageButton)view.findViewById(R.id.imageFavButton);
        lastFavIdText = (TextView) view.findViewById(R.id.last_favor_id);
        RenderView(view);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favButton.getTag() == "favor_btn_off") {
                    Favorite();
                } else {
                    DisableFavorite();
                }
            }
        });
        return view;
    }

    public void RenderView(View view){
        ModeModel modeModel=FileHelper.GetModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Custom");
        initWheel(view, R.id.color_vertical_wheel1);
        initWheel(view, R.id.color_vertical_wheel2);
        initWheel(view, R.id.color_vertical_wheel3);
        initSecondsWheel(view, R.id.time_vertical_wheel1);
        initSecondsWheel(view, R.id.time_vertical_wheel2);
        initSecondsWheel(view, R.id.time_vertical_wheel3);
        if(modeModel!=null) {
            WheelView wheel1 = (WheelView) view.findViewById(R.id.color_vertical_wheel1);
            wheel1.setCurrentItem(modeModel.getColorsWheelNumber().get(0));
            WheelView wheel2 = (WheelView) view.findViewById(R.id.color_vertical_wheel2);
            wheel2.setCurrentItem(modeModel.getColorsWheelNumber().get(1));
            WheelView wheel3 = (WheelView) view.findViewById(R.id.color_vertical_wheel3);
            wheel3.setCurrentItem(modeModel.getColorsWheelNumber().get(2));
            int colorId1 = getResources().getIdentifier(modeModel.getColors().get(0), "color", "com.example.jackgu.light");
            imageView1.setImageResource(colorId1);
            imageView1.setTag(modeModel.getColors().get(0));
            int colorId2 = getResources().getIdentifier(modeModel.getColors().get(1), "color", "com.example.jackgu.light");
            imageView2.setImageResource(colorId2);
            imageView2.setTag(modeModel.getColors().get(1));
            int colorId3 = getResources().getIdentifier(modeModel.getColors().get(2), "color", "com.example.jackgu.light");
            imageView3.setImageResource(colorId3);
            imageView3.setTag(modeModel.getColors().get(2));

            WheelView timewheel1 = (WheelView) view.findViewById(R.id.time_vertical_wheel1);
            timewheel1.setCurrentItem(modeModel.getSecondsWheelNumber().get(0));
            WheelView timewheel2 = (WheelView) view.findViewById(R.id.time_vertical_wheel2);
            timewheel2.setCurrentItem(modeModel.getSecondsWheelNumber().get(1));
            WheelView timewheel3 = (WheelView) view.findViewById(R.id.time_vertical_wheel3);
            timewheel3.setCurrentItem(modeModel.getSecondsWheelNumber().get(2));

            if (modeModel.getIsFavorite()){
                favButton.setImageResource(R.drawable.favor_btn_on);
                favButton.setTag("favor_btn_on");
            }else{
                favButton.setImageResource(R.drawable.favor_btn_off);
                favButton.setTag("favor_btn_off");
            }
        }
    }


    public void Favorite()
    {
        favButton.setImageResource(R.drawable.favor_btn_on);
        favButton.setTag("favor_btn_on");
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOn();

        String color1 = (String) imageView1.getTag();
        String color2 = (String) imageView2.getTag();
        String color3 = (String) imageView3.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.setMode("Custom");

        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        WheelView wheel2 = (WheelView) getView().findViewById(R.id.time_vertical_wheel2);
        WheelView wheel3 = (WheelView) getView().findViewById(R.id.time_vertical_wheel3);
        int index1 = wheel1.getCurrentItem();
        int index2 = wheel2.getCurrentItem();
        int index3 = wheel3.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        FloatSecondsWheelAdapter adapter2=(FloatSecondsWheelAdapter)wheel2.getViewAdapter();
        FloatSecondsWheelAdapter adapter3=(FloatSecondsWheelAdapter)wheel3.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        CharSequence second2 = adapter2.getItemText(index2);
        CharSequence second3 = adapter3.getItemText(index3);
        modeModel.addSecond(second1.toString());
        modeModel.addSecond(second2.toString());
        modeModel.addSecond(second3.toString());
        adapter1 = null;
        adapter2 = null;
        adapter3 = null;
        int no = FileHelper.AddMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json",modeModel);
        lastFavIdText.setText(no + "");
        //+favor for setting 这种情况只有通过圆盘操作才需要
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json","Custom",true, no);
    }

    public void DisableFavorite()
    {
        favButton.setImageResource(R.drawable.favor_btn_off);
        favButton.setTag("favor_btn_off");
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOff();

        String color1 = (String) imageView1.getTag();
        String color2 = (String) imageView2.getTag();
        String color3 = (String) imageView3.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        modeModel.addColor(color2);
        modeModel.addColor(color3);
        modeModel.setMode("Custom");

        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        WheelView wheel2 = (WheelView) getView().findViewById(R.id.time_vertical_wheel2);
        WheelView wheel3 = (WheelView) getView().findViewById(R.id.time_vertical_wheel3);
        int index1 = wheel1.getCurrentItem();
        int index2 = wheel2.getCurrentItem();
        int index3 = wheel3.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        FloatSecondsWheelAdapter adapter2=(FloatSecondsWheelAdapter)wheel2.getViewAdapter();
        FloatSecondsWheelAdapter adapter3=(FloatSecondsWheelAdapter)wheel3.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        CharSequence second2 = adapter2.getItemText(index2);
        CharSequence second3 = adapter3.getItemText(index3);
        modeModel.addSecond(second1.toString());
        modeModel.addSecond(second2.toString());
        modeModel.addSecond(second3.toString());
        adapter1 = null;
        adapter2 = null;
        adapter3 = null;
        FileHelper.RemoveMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json",modeModel);
        lastFavIdText.setText(-1 + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Custom", false, -1);
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

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            favButton.setImageResource(R.drawable.favor_btn_off);
            favButton.setTag("favor_btn_off");
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
            VerColorWheelAdapter adapter = (VerColorWheelAdapter)wheel.getViewAdapter();
            int firstItem = wheel.getFirstItem();
            String colorstr = adapter.getColorString(firstItem);
            int resourceId = getResources().getIdentifier(colorstr, "color", "com.example.jackgu.light");
            int id = wheel.getId();

            if(id == R.id.color_vertical_wheel1)
            {
                imageView1.setImageResource(resourceId);
                imageView1.setTag(colorstr);
            }
            if(id == R.id.color_vertical_wheel2)
            {
                imageView2.setImageResource(resourceId);
                imageView2.setTag(colorstr);
            }
            if(id == R.id.color_vertical_wheel3)
            {
                imageView3.setImageResource(resourceId);
                imageView3.setTag(colorstr);
            }
            adapter = null;
        }
    };

    private void initSecondsWheel(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
            wheel.setViewAdapter(new FloatSecondsWheelAdapter(getActivity()));
        wheel.setCurrentItem(2);
        wheel.setWheelBackground(R.color.grey_600);
        wheel.setDrawShadows(false);
        wheel.addChangingListener(changedSecondsListener);
        //wheel.addScrollingListener(scrolledListener);
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
        mActivity = getActivity();
    }

    private int getCurrentWheelIndex(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        return wheel.getCurrentItem();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            String color1 = (String) imageView1.getTag();
            String color2 = (String) imageView2.getTag();
            String color3 = (String) imageView3.getTag();
            int index1 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel1);
            int index2 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel2);
            int index3 =getCurrentWheelIndex(mParent, R.id.color_vertical_wheel3);
            ModeModel modeModel = new ModeModel();
            modeModel.addColor(color1,index1);
            modeModel.addColor(color2,index2);
            modeModel.addColor(color3,index3);
            modeModel.setMode("Custom");

            WheelView wheel1 = (WheelView) mParent.findViewById(R.id.time_vertical_wheel1);
            WheelView wheel2 = (WheelView) mParent.findViewById(R.id.time_vertical_wheel2);
            WheelView wheel3 = (WheelView) mParent.findViewById(R.id.time_vertical_wheel3);
            int timeindex1 = wheel1.getCurrentItem();
            int timeindex2 = wheel2.getCurrentItem();
            int timeindex3 = wheel3.getCurrentItem();
            FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
            FloatSecondsWheelAdapter adapter2=(FloatSecondsWheelAdapter)wheel2.getViewAdapter();
            FloatSecondsWheelAdapter adapter3=(FloatSecondsWheelAdapter)wheel3.getViewAdapter();
            CharSequence second1 = adapter1.getItemText(timeindex1);
            CharSequence second2 = adapter2.getItemText(timeindex2);
            CharSequence second3 = adapter3.getItemText(timeindex3);
            modeModel.addSecond(second1.toString(),timeindex1);
            modeModel.addSecond(second2.toString(),timeindex2);
            modeModel.addSecond(second3.toString(),timeindex3);
            modeModel.setIsFavorite(favButton.getTag().equals("favor_btn_on"));

            adapter1 = null;
            adapter2 = null;
            adapter3 = null;
            modeModel.setNo(Integer.parseInt(lastFavIdText.getText().toString()));
            FileHelper.AddModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", modeModel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
