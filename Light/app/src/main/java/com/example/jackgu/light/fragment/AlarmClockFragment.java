package com.example.jackgu.light.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.example.jackgu.light.HelpActivity;
import com.example.jackgu.light.R;
import com.example.jackgu.light.model.ClockModel;
import com.example.jackgu.light.model.ModeModel;
import com.example.jackgu.light.others.FileHelper;
import com.example.jackgu.light.view.TitleView;
import com.example.jackgu.light.wheel.OnWheelChangedListener;
import com.example.jackgu.light.wheel.OnWheelScrollListener;
import com.example.jackgu.light.wheel.WheelView;
import com.example.jackgu.light.wheel.adapters.ArrayWheelAdapter;
import com.example.jackgu.light.wheel.adapters.FloatSecondsWheelAdapter;
import com.example.jackgu.light.wheel.adapters.NumericWheelAdapter;
import com.example.jackgu.light.wheel.adapters.VerColorWheelAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlarmClockFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private TitleView mTitle;

    private RelativeLayout openClockPanel;
    private RelativeLayout closeClockPanel;
    private RelativeLayout modePanel;
    private TextView alarm_open_time;
    private TextView alarm_close_time;
    private TextView alarm_mode;
    private ImageButton favButton;

    public void setClockFavButtonOff(){
        favButton.setImageResource(R.drawable.favor_btn_off);
        favButton.setTag("favor_btn_off");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
        mParent = view;
        mTitle = (TitleView) view.findViewById(R.id.alarm_clock_title);
        mTitle.setTitle(R.string.alarm_clock_title);
        openClockPanel = (RelativeLayout) view.findViewById(R.id.set_open_clock_panel);
        closeClockPanel = (RelativeLayout) view.findViewById(R.id.set_close_clock_panel);
        modePanel = (RelativeLayout) view.findViewById(R.id.set_mode_panel);
        RelativeLayout openlayout = (RelativeLayout) view.findViewById(R.id.open_time);
        RelativeLayout closelayout = (RelativeLayout) view.findViewById(R.id.close_time);
        final RelativeLayout modelayout = (RelativeLayout) view.findViewById(R.id.choose_mode);
        openlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClockPanel.setVisibility(View.VISIBLE);
            }
        });
        closelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeClockPanel.setVisibility(View.VISIBLE);
            }
        });
        modelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePanel.setVisibility(View.VISIBLE);
            }
        });

        alarm_open_time = (TextView) view.findViewById(R.id.alarm_open_time);
        alarm_close_time = (TextView) view.findViewById(R.id.alarm_close_time);
        alarm_mode = (TextView) view.findViewById(R.id.alarm_mode);
        ImageView halfTransparentOpenPanel = (ImageView) view.findViewById(R.id.half_transparent_open_panel);
        ImageView halfTransparentClosePanel = (ImageView) view.findViewById(R.id.half_transparent_close_panel);
        halfTransparentOpenPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClockPanel.setVisibility(View.INVISIBLE);
                WheelView hourwheel = (WheelView) mParent.findViewById(R.id.clock_vertical_open_wheel1);
                WheelView minutewheel = (WheelView) mParent.findViewById(R.id.clock_vertical_open_wheel2);
                int hour = hourwheel.getCurrentItem() + 1;
                int minute = minutewheel.getCurrentItem() + 1;
                alarm_open_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
            }
        });
        halfTransparentClosePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeClockPanel.setVisibility(View.INVISIBLE);
                WheelView hourwheel = (WheelView) mParent.findViewById(R.id.clock_vertical_close_wheel1);
                WheelView minutewheel = (WheelView) mParent.findViewById(R.id.clock_vertical_close_wheel2);
                int hour = hourwheel.getCurrentItem() + 1;
                int minute = minutewheel.getCurrentItem() + 1;
                alarm_close_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
            }
        });

        ImageView halfTransparentModePanel = (ImageView) view.findViewById(R.id.half_transparent_mode_panel);
        halfTransparentModePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePanel.setVisibility(View.INVISIBLE);
                WheelView modewheel = (WheelView) mParent.findViewById(R.id.mode_vertical_wheel);
                ArrayWheelAdapter<String> adapter = (ArrayWheelAdapter<String>) modewheel.getViewAdapter();
                CharSequence modechar = adapter.getItemText(modewheel.getCurrentItem());
                String mode = modechar != null ? modechar.toString() : "";
                alarm_mode.setText(mode);
            }
        });

        favButton = (ImageButton) view.findViewById(R.id.imageFavButton);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json");
                if(modes.size() != 0) {
                    String favorState = (String) favButton.getTag();
                    if (favorState.equals("favor_btn_off")) {
                        favButton.setImageResource(R.drawable.favor_btn_on);
                        favButton.setTag("favor_btn_on");
                        ClockModel clockModel = new ClockModel();
                        String openTime = alarm_open_time.getText().toString();
                        String closeTime = alarm_close_time.getText().toString();
                        WheelView wheel = (WheelView) mParent.findViewById(R.id.mode_vertical_wheel);
                        int index = wheel.getCurrentItem();

                        ModeModel mode = modes.get(index);

                        clockModel.setCloseTime(closeTime);
                        clockModel.setOpenTime(openTime);
                        clockModel.setModeIndex(index);
                        clockModel.setModeName(mode.getMode());
                        clockModel.setModeText(alarm_mode.getText().toString());
                        FileHelper.AddClock(getActivity().getFilesDir().getPath().toString() + "/favorite_clock.json", clockModel);
                    } else {
                        favButton.setImageResource(R.drawable.favor_btn_off);
                        favButton.setTag("favor_btn_off");
                        ClockModel clockModel = new ClockModel();
                        String openTime = alarm_open_time.getText().toString();
                        String closeTime = alarm_close_time.getText().toString();
                        WheelView wheel = (WheelView) mParent.findViewById(R.id.mode_vertical_wheel);
                        int index = wheel.getCurrentItem();
                        ModeModel mode = modes.get(index);

                        clockModel.setCloseTime(closeTime);
                        clockModel.setOpenTime(openTime);
                        clockModel.setModeIndex(index);
                        clockModel.setModeName(mode.getMode());
                        clockModel.setModeText(alarm_mode.getText().toString());
                        FileHelper.RemoveClock(getActivity().getFilesDir().getPath().toString() + "/favorite_clock.json", clockModel);
                    }
                }
            }
        });
        RenderView(view);
        return view;
    }

    public void RenderView(View view) {
        initTimeWheel(view, R.id.clock_vertical_open_wheel1, "hour");
        initTimeWheel(view, R.id.clock_vertical_open_wheel2, "minute");
        initTimeWheel(view, R.id.clock_vertical_close_wheel1, "hour");
        initTimeWheel(view, R.id.clock_vertical_close_wheel2, "minute");
        initModeWheel(view, R.id.mode_vertical_wheel);
        ClockModel clockSetting = FileHelper.GetClockSetting(getActivity().getFilesDir().getPath().toString() + "/clock_setting.json");
        if (clockSetting != null) {
            String opentime = clockSetting.getOpenTime();
            String closetime = clockSetting.getCloseTime();
            String[] openArray = opentime.split(":");
            String[] closeArray = closetime.split(":");
            WheelView openwheel1 = (WheelView) view.findViewById(R.id.clock_vertical_open_wheel1);
            WheelView openwheel2 = (WheelView) view.findViewById(R.id.clock_vertical_open_wheel2);
            if (openArray.length >= 2) {
                openwheel1.setCurrentItem(Integer.parseInt(openArray[0]) - 1);
                openwheel2.setCurrentItem(Integer.parseInt(openArray[1]) - 1);
            }
            WheelView closewheel1 = (WheelView) view.findViewById(R.id.clock_vertical_close_wheel1);
            WheelView closewheel2 = (WheelView) view.findViewById(R.id.clock_vertical_close_wheel2);
            if (closeArray.length >= 2) {
                closewheel1.setCurrentItem(Integer.parseInt(closeArray[0]) - 1);
                closewheel2.setCurrentItem(Integer.parseInt(closeArray[1]) - 1);
            }

            WheelView modewheel = (WheelView) view.findViewById(R.id.mode_vertical_wheel);
            modewheel.setCurrentItem(clockSetting.getModeIndex());
            alarm_open_time.setText(opentime);
            alarm_close_time.setText(closetime);
            ArrayWheelAdapter<String> adapter = (ArrayWheelAdapter<String>) modewheel.getViewAdapter();
            String mode = adapter.getItemText(clockSetting.getModeIndex()).toString();
            alarm_mode.setText(mode);
            if (clockSetting.getIsFavorite()) {
                favButton.setImageResource(R.drawable.favor_btn_on);
                favButton.setTag("favor_btn_on");
            } else {
                favButton.setImageResource(R.drawable.favor_btn_off);
                favButton.setTag("favor_btn_off");
            }
        }
    }

    private void initTimeWheel(View view, int id, String type) {
        WheelView wheel = (WheelView) view.findViewById(id);
        if (type.equals("hour")) {
            wheel.setViewAdapter(new NumericWheelAdapter(getActivity(), 1, 24));
        } else {
            wheel.setViewAdapter(new NumericWheelAdapter(getActivity(), 1, 59));
        }
        wheel.setWheelBackground(R.color.grey_0);
        wheel.addScrollingListener(timeScrolledListener);
        wheel.setCurrentItem(0);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    OnWheelScrollListener timeScrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            favButton.setImageResource(R.drawable.favor_btn_off);
            favButton.setTag("favor_btn_off");
        }
    };

    private void initModeWheel(View view, int id) {
        WheelView wheel = (WheelView) view.findViewById(id);
        ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json");
        String[] items = GetModeNames(modes);
        wheel.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), items));
        wheel.addScrollingListener(modeScrolledListener);
        wheel.setCurrentItem(0);
        wheel.setWheelBackground(R.color.grey_0);
        wheel.setDrawShadows(false);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    OnWheelScrollListener modeScrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            favButton.setImageResource(R.drawable.favor_btn_off);
            favButton.setTag("favor_btn_off");
        }
    };

    private String[] GetModeNames(ArrayList<ModeModel> modes) {
        String[] strmodes = new String[modes.size()];
        for (int i = 0; i < modes.size(); i++) {
            String stringName = (modes.get(i).getMode() + "_mode_title").toLowerCase();
            int resourceStrId = getResources().getIdentifier(stringName, "string", "com.example.jackgu.light");
            strmodes[i] = getResources().getString(resourceStrId);
        }
        return strmodes;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

//    private void goHelpActivity() {
//        Intent intent = new Intent(mActivity, HelpActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ClockModel clockModel = new ClockModel();
            String openTime = !alarm_open_time.getText().toString().equals("") ? alarm_open_time.getText().toString() : "01:01";
            String closeTime = !alarm_close_time.getText().toString().equals("") ? alarm_close_time.getText().toString() : "01:01";
            WheelView wheel = (WheelView) mParent.findViewById(R.id.mode_vertical_wheel);
            int index = wheel.getCurrentItem();
            ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(getActivity().getFilesDir().getPath().toString() + "/favorite_mode.json");
            if (modes.size() > index) {
                ModeModel mode = modes.get(index);

                clockModel.setCloseTime(closeTime);
                clockModel.setOpenTime(openTime);
                clockModel.setModeIndex(index);
                clockModel.setIsFavorite(favButton.getTag().equals("favor_btn_on"));
                clockModel.setModeName(mode.getMode());
                FileHelper.AddClockSetting(getActivity().getFilesDir().getPath().toString() + "/clock_setting.json", clockModel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
