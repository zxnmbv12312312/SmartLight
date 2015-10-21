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

public class MonochromeModeFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private CircleImageView imageView1;

    private TitleView titleView;

    private ImageButton favButton;
    private TextView lastFavIdText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monochrome_mode, container, false);
        titleView = (TitleView) view.findViewById(R.id.monochrome_mode_title);
        titleView.setTitle(R.string.monochrome_mode_title);
        imageView1 = (CircleImageView)view.findViewById(R.id.image_color1);
        favButton = (ImageButton) view.findViewById(R.id.imageFavButton);
        lastFavIdText = (TextView) view.findViewById(R.id.last_favor_id);
        RenderView(view);
        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(favButton.getTag()=="favor_btn_off") {
                    Favorite();
                }else {
                    DisableFavorite();
                }
            }
        });
        return view;
    }

    public void RenderView(View view){
        initWheel(view, R.id.color_vertical_wheel1);
        initSecondsWheel(view, R.id.time_vertical_wheel1);

        ModeModel modeModel=FileHelper.GetModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Monochrome");
        if(modeModel!=null) {
            WheelView colorwheel1 = (WheelView) view.findViewById(R.id.color_vertical_wheel1);
            colorwheel1.setCurrentItem(modeModel.getColorsWheelNumber().get(0));
            int colorId1 = getResources().getIdentifier(modeModel.getColors().get(0), "color", "com.example.jackgu.light");
            imageView1.setImageResource(colorId1);
            imageView1.setTag(modeModel.getColors().get(0));

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
        favButton.setTag("favor_btn_on");
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOn();
        String color1 = (String)imageView1.getTag();

        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);

        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        int index1 = wheel1.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        modeModel.addSecond(second1.toString());
        adapter1 = null;
        modeModel.setMode("Monochrome");
        int no = FileHelper.AddMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(no + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Monochrome", true, no);
    }

    public void DisableFavorite(){
        favButton.setImageResource(R.drawable.favor_btn_off);
        favButton.setTag("favor_btn_off");
        MainFragment mainFragment = (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setModeFavButtonOff();

        String color1 = (String)imageView1.getTag();
        ModeModel modeModel = new ModeModel();
        modeModel.addColor(color1);
        WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
        int index1 = wheel1.getCurrentItem();
        FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
        CharSequence second1 = adapter1.getItemText(index1);
        modeModel.addSecond(second1.toString());
        adapter1 = null;
        modeModel.setMode("Monochrome");
        FileHelper.RemoveMode(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json", modeModel);
        lastFavIdText.setText(-1 + "");
        FileHelper.FavorModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", "Monochrome", false, -1);
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
            adapter = null;
        }
    };


    private void initSecondsWheel(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        wheel.setViewAdapter(new FloatSecondsWheelAdapter(getActivity()));
        wheel.setCurrentItem(4);
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
        mActivity = getActivity();
        mParent = getView();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden)
        {
            String color1 = (String)imageView1.getTag();
            WheelView colorwheel1 = (WheelView) getView().findViewById(R.id.color_vertical_wheel1);
            int index1 = colorwheel1.getCurrentItem();
            ModeModel modeModel = new ModeModel();
            modeModel.addColor(color1,index1);

            WheelView wheel1 = (WheelView) getView().findViewById(R.id.time_vertical_wheel1);
            int timeindex1 = wheel1.getCurrentItem();
            FloatSecondsWheelAdapter adapter1=(FloatSecondsWheelAdapter)wheel1.getViewAdapter();
            CharSequence second1 = adapter1.getItemText(timeindex1);
            modeModel.addSecond(second1.toString(),timeindex1);
            adapter1 = null;
            modeModel.setMode("Monochrome");
            modeModel.setNo(Integer.parseInt(lastFavIdText.getText().toString()));
            FileHelper.AddModeSetting(getActivity().getFilesDir().getPath().toString() + "/mode_setting.json", modeModel);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
