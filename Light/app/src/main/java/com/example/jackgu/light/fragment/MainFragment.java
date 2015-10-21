package com.example.jackgu.light.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.jackgu.light.MainActivity;
import com.example.jackgu.light.R;
import com.example.jackgu.light.model.ColorModel;
import com.example.jackgu.light.model.ModeModel;
import com.example.jackgu.light.others.BluetoothHelper;
import com.example.jackgu.light.others.BluetoothLeService;
import com.example.jackgu.light.others.CircleIconAdapter;
import com.example.jackgu.light.others.CommandBuilder;
import com.example.jackgu.light.others.FileHelper;
import com.example.jackgu.light.others.FragmentHelper;
import com.example.jackgu.light.others.MaterialColor;
import com.example.jackgu.light.others.MaterialColorAdapter;
import com.example.jackgu.light.others.SecondsHelper;
import com.example.jackgu.light.view.TitleView;
import com.lukedeighton.wheelview.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainFragment extends Fragment {
    private View mParent;
    private TitleView mTitle;
    private WheelView colorWheel;
    private WheelView shadowWheel;
    private WheelView modeWheel;
    private ImageButton centerButton;
    private ImageButton pieModeButton;
    private ImageButton pieColorButton;
    private ImageButton pieClockButton;
    private MainActivity mActivity;
    private ImageButton colorFavoriteButton;
    private ImageButton modeFavoriteButton;
    private Button editButton;
    private static final int ITEM_COUNT = 36;
    private static final int SHADOW_ITEM_COUNT = 18;
    private String filePath;
    private HashMap<String,byte[]> command=new HashMap<String,byte[]>();
    private String comandType = "Color";

    public void setModeFavButtonOn() {
        modeFavoriteButton.setImageResource(R.drawable.favor_btn_on);
        modeFavoriteButton.setTag("favor_btn_on");
    }

    public void setModeFavButtonOff() {
        modeFavoriteButton.setImageResource(R.drawable.favor_btn_off);
        modeFavoriteButton.setTag("favor_btn_off");
    }

    public void setColorFavButtonOff(ColorModel colorModel) {
        MaterialColorAdapter adapter = (MaterialColorAdapter) colorWheel.getAdapter();
        int position = colorWheel.getPosition() % ITEM_COUNT;
        if (position < 0) {
            position = ITEM_COUNT + position;
        }
        Map.Entry<String, Integer> color = adapter.getColor(position);
        String colorStr = color.getKey();

        int shadowPosition = shadowWheel.getPosition() % SHADOW_ITEM_COUNT;
        if (shadowPosition < 0) {
            shadowPosition = SHADOW_ITEM_COUNT + shadowPosition;
        }
        if (shadowPosition > 9) {
            shadowPosition = 9 - (shadowPosition - 9);
        }
        if (shadowPosition == colorModel.getShadow() && colorModel.getColor().equals(colorStr)) {
            colorFavoriteButton.setImageResource(R.drawable.favor_btn_off);
            colorFavoriteButton.setTag("favor_btn_off");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        filePath = getActivity().getFilesDir().getPath().toString();
        mTitle = (TitleView) view.findViewById(R.id.main_title);
        mTitle.setTitle(R.string.title_main);
        colorFavoriteButton = (ImageButton) view.findViewById(R.id.colorFavButton);
        modeFavoriteButton = (ImageButton) view.findViewById(R.id.modeFavButton);
        editButton = (Button) view.findViewById(R.id.edit_button);
        centerButton = (ImageButton) view.findViewById(R.id.centerButton);
        //cicle
        shadowWheel = (WheelView) view.findViewById(R.id.shadow_view);
        colorWheel = (WheelView) view.findViewById(R.id.wheelview);
        mActivity = (MainActivity)getActivity();
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(ITEM_COUNT);
        for (int i = 0; i < ITEM_COUNT; i++) {
            Map.Entry<String, Integer> entry = MaterialColor.getColor(mActivity, "c\\d*_pick$", i);
            entries.add(entry);
        }
        colorWheel.setAdapter(new MaterialColorAdapter(entries));
        colorWheel.setSelectionColor(entries.get(0).getValue());
        colorWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, int position) {
                colorFavoriteButton.setImageResource(R.drawable.favor_btn_off);
                colorFavoriteButton.setTag("favor_btn_off");
                centerButton.setImageResource(R.drawable.swith_off);
                centerButton.setTag("swith_off");
            }
        });
        colorFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String favorState = (String) colorFavoriteButton.getTag();
                if (colorWheel.getVisibility() == View.VISIBLE) {
                    if (favorState.equals("favor_btn_off")) {
                        FavoriteColor();
                    } else {
                        DisableFavoriteColor();
                    }
                }
            }
        });
        ModeModel lightmodeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Lighting");
        if (lightmodeModel != null && lightmodeModel.getIsFavorite()) {
            modeFavoriteButton.setImageResource(R.drawable.favor_btn_on);
            modeFavoriteButton.setTag("favor_btn_on");
        }
        modeFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String favorState = (String) modeFavoriteButton.getTag();
                if (modeWheel.getVisibility() == View.VISIBLE) {
                    if (favorState.equals("favor_btn_off")) {
                        FavoriteMode();
                    } else {
                        DisableFavoriteMode();
                    }
                }
            }
        });

        List<Map.Entry<String, Integer>> shadowentries = new ArrayList<Map.Entry<String, Integer>>(SHADOW_ITEM_COUNT);
        for (int i = 0; i < SHADOW_ITEM_COUNT; i++) {
            Map.Entry<String, Integer> entry = MaterialColor.getColor(mActivity, "shadow_grey_\\d*$", i);
            shadowentries.add(entry);
        }
        shadowWheel.setAdapter(new MaterialColorAdapter(shadowentries));
        shadowWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, int position) {
                colorFavoriteButton.setImageResource(R.drawable.favor_btn_off);
                colorFavoriteButton.setTag("favor_btn_off");
                centerButton.setImageResource(R.drawable.swith_off);
                centerButton.setTag("swith_off");
            }
        });
        shadowWheel.setSelectionColor(shadowentries.get(0).getValue());

        modeWheel = (WheelView) view.findViewById(R.id.mode_view);
        ArrayList<Integer> modeIcons = initWheelIcons();
        CircleIconAdapter circleIconAdapter = new CircleIconAdapter(mActivity, modeIcons);
        circleIconAdapter.setItem(0, R.drawable.lighting_on);
        modeWheel.setAdapter(circleIconAdapter);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = modeWheel.getPosition() % 6;
                if (position < 0) {
                    position = 6 + position;
                }
                switch (position) {
                    case 0:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.LightingModeFragment", R.id.fragment_container, "LightingModeFragment");
                        break;
                    case 1:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.MonochromeModeFragment", R.id.fragment_container, "MonochromeModeFragment");
                        break;
                    case 2:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.FireCrackerModeFragment", R.id.fragment_container, "FireCrackerModeFragment");
                        break;
                    case 3:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.GradualChangeFragment", R.id.fragment_container, "GradualChangeFragment");
                        break;
                    case 4:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.HoppingModeFragment", R.id.fragment_container, "HoppingModeFragment");
                        break;
                    case 5:
                        FragmentHelper.RecreateFragemnt(getFragmentManager(), "com.example.jackgu.light.fragment.CustomModeFragment", R.id.fragment_container, "CustomModeFragment");
                        break;
                }
            }
        });

        modeWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, int position) {
                CircleIconAdapter adapter = (CircleIconAdapter) parent.getAdapter();
                selectWheelIcon(position, adapter);
                parent.setAdapter(adapter);
                ModeModel modeModel = null;
                switch (position) {
                    case 0:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Lighting");
                        break;
                    case 1:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Monochrome");
                        break;
                    case 2:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "FireCracker");
                        break;
                    case 3:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Gradual_Change");
                        break;
                    case 4:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Hopping");
                        break;
                    case 5:
                        modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Custom");
                        break;
                }
                if (modeModel != null && modeModel.getIsFavorite()) {
                    modeFavoriteButton.setImageResource(R.drawable.favor_btn_on);
                    modeFavoriteButton.setTag("favor_btn_on");
                } else {
                    modeFavoriteButton.setImageResource(R.drawable.favor_btn_off);
                    modeFavoriteButton.setTag("favor_btn_off");
                }
                centerButton.setImageResource(R.drawable.swith_off);
                centerButton.setTag("swith_off");
            }
        });


        pieModeButton = (ImageButton) view.findViewById(R.id.pieButton1);
        pieModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorFavoriteButton.setVisibility(View.INVISIBLE);
                modeFavoriteButton.setVisibility(View.VISIBLE);
                pieModeButton.setImageResource(R.drawable.pie_menu_1_forcus);
                pieColorButton.setImageResource(R.drawable.pie_menu_2_normal);
                colorWheel.setVisibility(View.INVISIBLE);
                shadowWheel.setVisibility(View.INVISIBLE);
                modeWheel.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                centerButton.setImageResource(R.drawable.swith_off);
                centerButton.setTag("swith_off");
            }
        });

        pieColorButton = (ImageButton) view.findViewById(R.id.pieButton2);
        pieColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeFavoriteButton.setVisibility(View.INVISIBLE);
                colorFavoriteButton.setVisibility(View.VISIBLE);
                pieColorButton.setImageResource(R.drawable.pie_menu_2_forcus);
                pieModeButton.setImageResource(R.drawable.pie_menu_1_normal);
                colorWheel.setVisibility(View.VISIBLE);
                shadowWheel.setVisibility(View.VISIBLE);
                modeWheel.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.INVISIBLE);
                centerButton.setImageResource(R.drawable.swith_off);
                centerButton.setTag("swith_off");
            }
        });
        pieColorButton.setImageResource(R.drawable.pie_menu_2_forcus);
        pieModeButton.setImageResource(R.drawable.pie_menu_1_normal);

        pieClockButton = (ImageButton) view.findViewById(R.id.pieButton3);
        pieClockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentHelper.HideAllFragemnt(getFragmentManager());
                FragmentHelper.ShowFragemnt(getFragmentManager(),"com.example.jackgu.light.fragment.AlarmClockFragment");
                //getFragmentManager(), "com.example.jackgu.light.fragment.AlarmClockFragment", R.id.fragment_container, "AlarmClockFragment");
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorWheel.getVisibility() == View.VISIBLE) {
                    if (centerButton.getTag().equals("swith_off")) {
                        centerButton.setImageResource(R.drawable.swith_on);
                        centerButton.setTag("swith_on");
                        comandType = "Color";
                        BluetoothHelper.setCommandType(comandType);
                        //send bluetooth
                        ArrayList<BluetoothDevice> deviceArrayList = BluetoothHelper.getCheckedDevices();
                        if (deviceArrayList.size() > 0) {
                            ColorModel colorModel = getCurrentColorModule();
                            command = CommandBuilder.BuildColorCommand(colorModel, getResources());
                            BluetoothHelper.setCommands(command);
                            for (int i = 0; i < deviceArrayList.size(); i++) {
                                BluetoothDevice device = deviceArrayList.get(i);
                                String address =device.getAddress();
                                boolean ok= BluetoothLeService.getInstance().connect(address);
                                if(!ok) {
                                    BluetoothLeService.getBtGatt().discoverServices();
                                }
                                BluetoothLeService.getInstance().waitIdle(1000);
                            }
                        }
                    } else {
                        centerButton.setImageResource(R.drawable.swith_off);
                        centerButton.setTag("swith_off");
                    }
                }
                if (modeWheel.getVisibility() == View.VISIBLE) {
                    if (centerButton.getTag().equals("swith_off")) {
                        centerButton.setImageResource(R.drawable.swith_on);
                        centerButton.setTag("swith_on");
                        ArrayList<BluetoothDevice> deviceArrayList = BluetoothHelper.getCheckedDevices();
                        if (deviceArrayList.size() > 0) {
                            int position = modeWheel.getPosition() % 6;
                            if (position < 0) {
                                position = 6 + position;
                            }
                            ModeModel modeModel = null;
                            switch (position) {
                                case 0:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Lighting");
                                    command = CommandBuilder.BuildLightCommand(modeModel, getResources());
                                    comandType = "Lighting";
                                    break;
                                case 1:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Monochrome");
                                    command = CommandBuilder.BuildMonochromeCommand(modeModel, getResources());
                                    comandType = "Monochrome";
                                    break;
                                case 2:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "FireCracker");
                                    command = CommandBuilder.BuildFireCrackerCommand(modeModel, getResources());
                                    comandType = "FireCracker";
                                    break;
                                case 3:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Gradual_Change");
                                    command = CommandBuilder.BuildGradualChangeCommand(modeModel, getResources());
                                    comandType = "Gradual_Change";
                                    break;
                                case 4:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Hopping");
                                    command = CommandBuilder.BuildHoppingCommand(modeModel, getResources());
                                    comandType = "Hopping";
                                    break;
                                case 5:
                                    modeModel = FileHelper.GetModeSetting(filePath + "/mode_setting.json", "Custom");
                                    command = CommandBuilder.BuildCustomCommand(modeModel, getResources());
                                    comandType = "Custom";
                                    break;
                            }
                            BluetoothHelper.setCommandType(comandType);
                            BluetoothHelper.setCommands(command);
                            for (int i = 0; i < deviceArrayList.size(); i++) {
                                BluetoothDevice device = deviceArrayList.get(i);
                                String address =device.getAddress();
                                boolean ok= BluetoothLeService.getInstance().connect(address);
                                if(!ok) {
                                    BluetoothLeService.getBtGatt().discoverServices();
                                }
                                BluetoothLeService.getInstance().waitIdle(1000);
                            }
                        }
                    } else {
                        centerButton.setImageResource(R.drawable.swith_off);
                        centerButton.setTag("swith_off");
                    }
                }

            }
        });

        return view;
    }

    private void DisableFavoriteMode() {
        modeFavoriteButton.setImageResource(R.drawable.favor_btn_off);
        modeFavoriteButton.setTag("favor_btn_off");
        int position = modeWheel.getPosition() % 6;
        if (position < 0) {
            position = 6 + position;
        }
        switch (position) {
            case 0:
                LightingModeFragment lightingModeFragment = (LightingModeFragment) getFragmentManager().findFragmentByTag("LightingModeFragment");
                lightingModeFragment.DisableFavorite();
                break;
            case 1:
                MonochromeModeFragment monochromeModeFragment = (MonochromeModeFragment) getFragmentManager().findFragmentByTag("MonochromeModeFragment");
                monochromeModeFragment.DisableFavorite();
                break;
            case 2:
                FireCrackerModeFragment fireCrackerModeFragment = (FireCrackerModeFragment) getFragmentManager().findFragmentByTag("FireCrackerModeFragment");
                fireCrackerModeFragment.DisableFavorite();
                break;
            case 3:
                GradualChangeFragment gradualChangeFragment = (GradualChangeFragment) getFragmentManager().findFragmentByTag("GradualChangeFragment");
                gradualChangeFragment.DisableFavorite();
                break;
            case 4:
                HoppingModeFragment hoppingModeFragment = (HoppingModeFragment) getFragmentManager().findFragmentByTag("HoppingModeFragment");
                hoppingModeFragment.DisableFavorite();
                break;
            case 5:
                CustomModeFragment customModeFragment = (CustomModeFragment) getFragmentManager().findFragmentByTag("CustomModeFragment");
                customModeFragment.DisableFavorite();
                break;
        }
    }

    private void FavoriteMode() {
        modeFavoriteButton.setImageResource(R.drawable.favor_btn_on);
        modeFavoriteButton.setTag("favor_btn_on");
        int position = modeWheel.getPosition() % 6;
        if (position < 0) {
            position = 6 + position;
        }
        switch (position) {
            case 0:
                LightingModeFragment lightingModeFragment = (LightingModeFragment) getFragmentManager().findFragmentByTag("LightingModeFragment");
                lightingModeFragment.Favorite();
                break;
            case 1:
                MonochromeModeFragment monochromeModeFragment = (MonochromeModeFragment) getFragmentManager().findFragmentByTag("MonochromeModeFragment");
                monochromeModeFragment.Favorite();
                break;
            case 2:
                FireCrackerModeFragment fireCrackerModeFragment = (FireCrackerModeFragment) getFragmentManager().findFragmentByTag("FireCrackerModeFragment");
                fireCrackerModeFragment.Favorite();
                break;
            case 3:
                GradualChangeFragment gradualChangeFragment = (GradualChangeFragment) getFragmentManager().findFragmentByTag("GradualChangeFragment");
                gradualChangeFragment.Favorite();
                break;
            case 4:
                HoppingModeFragment hoppingModeFragment = (HoppingModeFragment) getFragmentManager().findFragmentByTag("HoppingModeFragment");
                hoppingModeFragment.Favorite();
                break;
            case 5:
                CustomModeFragment customModeFragment = (CustomModeFragment) getFragmentManager().findFragmentByTag("CustomModeFragment");
                customModeFragment.Favorite();
                break;
        }
    }

    private ColorModel getCurrentColorModule() {
        MaterialColorAdapter adapter = (MaterialColorAdapter) colorWheel.getAdapter();
        int position = colorWheel.getPosition() % ITEM_COUNT;
        if (position < 0) {
            position = ITEM_COUNT + position;
        }
        Map.Entry<String, Integer> color = adapter.getColor(position);
        String colorStr = color.getKey();

        int shadowPosition = shadowWheel.getPosition() % SHADOW_ITEM_COUNT;
        if (shadowPosition < 0) {
            shadowPosition = SHADOW_ITEM_COUNT + shadowPosition;
        }
        if (shadowPosition > 9) {
            shadowPosition = 9 - (shadowPosition - 9);
        }

        ColorModel colorModel = new ColorModel();
        colorModel.setColor(colorStr);
        colorModel.setShadow(shadowPosition);
        adapter = null;
        return colorModel;
    }

    private void DisableFavoriteColor() {
        colorFavoriteButton.setImageResource(R.drawable.favor_btn_off);
        colorFavoriteButton.setTag("favor_btn_off");
        ColorModel colorModel = getCurrentColorModule();
        FileHelper.RemoveColor(filePath + "/favorite_color.json", colorModel);
    }

    private void FavoriteColor() {
        colorFavoriteButton.setImageResource(R.drawable.favor_btn_on);
        colorFavoriteButton.setTag("favor_btn_on");
        ColorModel colorModel = getCurrentColorModule();
        FileHelper.AddColor(filePath + "/favorite_color.json", colorModel);
    }

    private ArrayList<Integer> initWheelIcons() {
        ArrayList<Integer> modeIcons = new ArrayList<Integer>();
        modeIcons.add(R.drawable.lighting_off);
        modeIcons.add(R.drawable.monochrome_off);
        modeIcons.add(R.drawable.firecracker_off);
        modeIcons.add(R.drawable.gradual_change_off);
        modeIcons.add(R.drawable.hopping_off);
        //modeIcons.add(R.drawable.reset_off);
        modeIcons.add(R.drawable.custom_off);
        return modeIcons;
    }

    private void selectWheelIcon(int position, CircleIconAdapter adapter) {
        adapter.setItem(0, R.drawable.lighting_off);
        adapter.setItem(1, R.drawable.monochrome_off);
        adapter.setItem(2, R.drawable.firecracker_off);
        adapter.setItem(3, R.drawable.gradual_change_off);
        adapter.setItem(4, R.drawable.hopping_off);
        //adapter.setItem(5, R.drawable.reset_off);
        adapter.setItem(5, R.drawable.custom_off);
        switch (position) {
            case 0:
                adapter.setItem(position, R.drawable.lighting_on);
                break;
            case 1:
                adapter.setItem(position, R.drawable.monochrome_on);
                break;
            case 2:
                adapter.setItem(position, R.drawable.firecracker_on);
                break;
            case 3:
                adapter.setItem(position, R.drawable.gradual_change_on);
                break;
            case 4:
                adapter.setItem(position, R.drawable.hopping_on);
                break;
//            case 5:
//                adapter.setItem(position, R.drawable.reset_on);
//                break;
            case 5:
                adapter.setItem(position, R.drawable.custom_on);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
