package com.example.jackgu.light.fragment;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

//import com.example.jackgu.light.HelpActivity;
import com.example.jackgu.light.MainActivity;
import com.example.jackgu.light.R;
import com.example.jackgu.light.model.ClockModel;
import com.example.jackgu.light.model.ColorModel;
import com.example.jackgu.light.model.ModeModel;
import com.example.jackgu.light.others.BluetoothHelper;
import com.example.jackgu.light.others.BluetoothLeService;
import com.example.jackgu.light.others.CommandBuilder;
import com.example.jackgu.light.others.FileHelper;
import com.example.jackgu.light.others.FragmentHelper;
import com.example.jackgu.light.view.TitleView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class FavoritesFragment extends Fragment {
    private View mParent;
    private TitleView mTitle;
    private MainActivity mActivity;
    private RelativeLayout deletePanel;

    private int deleteModeId;
    private int deleteColorId;
    private int deleteClockId;
    private String filePath;
    private HashMap<String,byte[]> command=new HashMap<String,byte[]>();
    private String comandType = "Color";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mTitle = (TitleView) view.findViewById(R.id.favorites_title);
        mTitle.setTitle(R.string.title_favorites);
        deletePanel = (RelativeLayout)view.findViewById(R.id.delete_panel);
        mParent = view;
        mActivity = (MainActivity)getActivity();
        filePath = getActivity().getFilesDir().getPath().toString();
        RenderView(mParent);

        return view;
    }

    public void RenderView(View view){
        ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(filePath + "/favorite_mode.json");
        LinearLayout modeLayout = (LinearLayout) view.findViewById(R.id.favorites_mode_panel);
        modeLayout.removeAllViews();
        for (int i = 0; i < modes.size(); i++) {
            ModeModel modeModel = modes.get(i);
            LinearLayout linemode = new LinearLayout(getActivity());
            linemode.setTag(i);
            linemode.setPadding(60, 10, 60, 10);
            linemode.setOrientation(LinearLayout.HORIZONTAL);
            linemode.setGravity(Gravity.CENTER_VERTICAL);
            linemode.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView imagePointView = new ImageView(getActivity());
            LinearLayout.LayoutParams pointlayoutParams = new LinearLayout.LayoutParams(60, 30 );
            pointlayoutParams.rightMargin=10;
            imagePointView.setLayoutParams(pointlayoutParams);
            imagePointView.setImageResource(R.drawable.favor_point);
            linemode.addView(imagePointView);

            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams imagelayoutParams = new LinearLayout.LayoutParams(40, 40 );
            imagelayoutParams.rightMargin=10;
            imageView.setLayoutParams(imagelayoutParams);
            String drawableName = (modeModel.getMode() + "_off").toLowerCase();
            int resourceId = getResources().getIdentifier(drawableName, "drawable", "com.example.jackgu.light");
            imageView.setImageResource(resourceId);
            linemode.addView(imageView);

            TextView textView = new TextView(getActivity());
            String stringName = (modeModel.getMode() + "_mode_title").toLowerCase();
            int resourceStrId = getResources().getIdentifier(stringName, "string", "com.example.jackgu.light");
            textView.setText(resourceStrId);
            textView.setWidth(200);
            linemode.addView(textView);

            ArrayList<String> colors = modeModel.getColors();
            for (int j = 0; j < colors.size(); j++) {
                ImageView imageColorView = new ImageView(getActivity());
                LinearLayout.LayoutParams colorlayoutParams = new LinearLayout.LayoutParams(40, 40 );
                colorlayoutParams.rightMargin = 10;
                imageColorView.setLayoutParams(colorlayoutParams);
                int colorId = getResources().getIdentifier(colors.get(j), "color", "com.example.jackgu.light");
                imageColorView.setImageResource(colorId);
                imageColorView.setBackgroundResource(R.drawable.imageborder);
                linemode.addView(imageColorView);
            }
            linemode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //sends info to bluetooth
                    int index=(int)v.getTag();
                    ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(filePath + "/favorite_mode.json");
                    ModeModel modeModel = modes.get(index);
                    ArrayList<BluetoothDevice> deviceArrayList = BluetoothHelper.getCheckedDevices();
                    if (deviceArrayList.size() > 0) {
                        String mode = modeModel.getMode();
                        if(mode.equals("Lighting")) {
                            command = CommandBuilder.BuildLightCommand(modeModel, getResources());
                            comandType = "Lighting";
                        }
                        if(mode.equals("Monochrome")) {
                            command = CommandBuilder.BuildMonochromeCommand(modeModel, getResources());
                            comandType = "Monochrome";
                        }
                        if(mode.equals("FireCracker")) {
                                command = CommandBuilder.BuildFireCrackerCommand(modeModel, getResources());
                                comandType = "FireCracker";
                        }
                        if(mode.equals("Gradual_Change")) {
                                command = CommandBuilder.BuildGradualChangeCommand(modeModel, getResources());
                                comandType = "Gradual_Change";
                        }
                        if(mode.equals("Hopping")) {
                                command = CommandBuilder.BuildHoppingCommand(modeModel, getResources());
                                comandType = "Hopping";
                        }
                        if(mode.equals("Custom")) {
                                command = CommandBuilder.BuildCustomCommand(modeModel, getResources());
                                comandType = "Custom";
                        }
                        BluetoothHelper.setCommands(command);
                        BluetoothHelper.setCommandType(comandType);
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
                }
            });
            linemode.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deletePanel.setVisibility(View.VISIBLE);
                    deleteModeId = Integer.parseInt(v.getTag().toString());
                    Button yesBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_yes);
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<ModeModel> modes = FileHelper.GetAllModeRecord(filePath + "/favorite_mode.json");
                            ModeModel mode = modes.get(deleteModeId);
                            try {
                                String modeName = mode.getMode();
                                Fragment fragment = FragmentHelper.GetFragemnt(getFragmentManager(), modeName.replace("_",""));
                                Method disableFavorite = fragment.getClass().getDeclaredMethod("DisableFavorite");
                                disableFavorite.invoke(fragment);
                            }catch (Exception ex){
                                System.out.println("disable favor failed...");
                            }
                            //FileHelper.RemoveMode(filePath + "/favorite_mode.json", deleteModeId);
                            deletePanel.setVisibility(View.INVISIBLE);
                            RenderView(mParent);
                        }
                    });
                    Button noBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_no);
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePanel.setVisibility(View.INVISIBLE);
                        }
                    });
                    return false;
                }
            });
            modeLayout.addView(linemode);
        }

        ArrayList<ColorModel> colors = FileHelper.GetAllColorRecord(filePath + "/favorite_color.json");
        LinearLayout colorLayout = (LinearLayout) view.findViewById(R.id.favorites_color_panel);
        colorLayout.removeAllViews();
        for (int i = 0; i < colors.size(); i++) {
            CircleImageView circleImageView = new CircleImageView(getActivity());
            LinearLayout.LayoutParams colorlayoutParams = new LinearLayout.LayoutParams(150, 150 );
            colorlayoutParams.rightMargin = 15;
            colorlayoutParams.topMargin = 30;
            colorlayoutParams.bottomMargin = 30;
            circleImageView.setLayoutParams(colorlayoutParams);
            int colorId = getResources().getIdentifier(colors.get(i).getColor(), "color", "com.example.jackgu.light");
            circleImageView.setImageResource(colorId);
            circleImageView.setTag(i);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send operation command;
                    ArrayList<ColorModel> colors = FileHelper.GetAllColorRecord(filePath + "/favorite_color.json");
                    int index = (int)v.getTag();
                    ColorModel color = colors.get(index);
                    ArrayList<BluetoothDevice> deviceArrayList = BluetoothHelper.getCheckedDevices();
                    command = CommandBuilder.BuildColorCommand(color,getResources());
                    comandType = "Color";
                    BluetoothHelper.setCommands(command);
                    BluetoothHelper.setCommandType(comandType);
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
            });
            circleImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deletePanel.setVisibility(View.VISIBLE);
                    deleteColorId = Integer.parseInt(v.getTag().toString());
                    Button yesBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_yes);
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ColorModel colorModel = FileHelper.GetColor(filePath + "/favorite_color.json", deleteColorId);
                            MainFragment main =  (MainFragment)getFragmentManager().findFragmentById(R.id.fragment_main);
                            main.setColorFavButtonOff(colorModel);
                            FileHelper.RemoveColor(filePath + "/favorite_color.json", deleteColorId);
                            deletePanel.setVisibility(View.INVISIBLE);
                            RenderView(mParent);
                        }
                    });
                    Button noBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_no);
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePanel.setVisibility(View.INVISIBLE);
                        }
                    });
                    return false;
                }
            });

            colorLayout.addView(circleImageView);
        }


        ArrayList<ClockModel> clocks = FileHelper.GetAllClockRecord(filePath + "/favorite_clock.json");
        final LinearLayout clockLayout = (LinearLayout) view.findViewById(R.id.favorites_clock_panel);
        clockLayout.removeAllViews();
        for (int i = 0; i < clocks.size(); i++) {
            RelativeLayout lineclock = new RelativeLayout(getActivity());
            lineclock.setPadding(60, 10, 60, 10);
            lineclock.setTag(i);

            TextView textView = new TextView(getActivity());
            textView.setText(clocks.get(i).getModeText());
            RelativeLayout.LayoutParams modelayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            modelayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textView.setLayoutParams(modelayoutParams);
            lineclock.addView(textView);

            LinearLayout centerLayout = new LinearLayout(getActivity());
            RelativeLayout.LayoutParams centerlayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            centerlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            centerLayout.setLayoutParams(centerlayoutParams);

            TextView opentextView = new TextView(getActivity());
            opentextView.setText(clocks.get(i).getOpenTime());
            centerLayout.addView(opentextView);

            TextView textView1 = new TextView(getActivity());
            textView1.setText("-");
            centerLayout.addView(textView1);

            TextView closetextView = new TextView(getActivity());
            closetextView.setText(clocks.get(i).getCloseTime());
            centerLayout.addView(closetextView);
            lineclock.addView(centerLayout);

            Switch switchbtn = new Switch(getActivity());
            RelativeLayout.LayoutParams switchlayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            switchlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            switchbtn.setLayoutParams(switchlayoutParams);
            switchbtn.setTag(i);
            switchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(v.getTag().toString());
                    ClockModel clockModel = FileHelper.GetClock(filePath + "/favorite_clock.json", id);
                    ModeModel modeModel = FileHelper.GetMode(filePath + "/favorite_mode.json", clockModel.getModeIndex());
                    String openTime =clockModel.getOpenTime();
                    String[] times = openTime.split(":");
                    int hour = (int)Integer.parseInt(times[0]);
                    int minute = (int)Integer.parseInt(times[1]);
                    HashMap<String,byte[]> comand = CommandBuilder.BuildAlarmCommand(id,hour,minute,modeModel,getResources());
                    BluetoothHelper.setCommandType("Alarm");
                    BluetoothHelper.setCommands(comand);
                    ArrayList<BluetoothDevice> deviceArrayList = BluetoothHelper.getCheckedDevices();
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
            });
            lineclock.addView(switchbtn);

            lineclock.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deletePanel.setVisibility(View.VISIBLE);
                    deleteClockId = Integer.parseInt(v.getTag().toString());
                    Button yesBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_yes);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ClockModel clockModel = FileHelper.GetClock(getActivity().getFilesDir().getPath().toString() + "/favorite_clock.json", deleteClockId);
                            AlarmClockFragment clockFragment =  (AlarmClockFragment)getFragmentManager().findFragmentByTag("AlarmClockFragment");
                            clockFragment.setClockFavButtonOff();
                            FileHelper.RemoveClock(filePath + "/favorite_clock.json", deleteClockId);
                            deletePanel.setVisibility(View.INVISIBLE);
                            RenderView(mParent);
                        }
                    });
                    Button noBtn =(Button)deletePanel.findViewById(R.id.favorites_delete_no);
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePanel.setVisibility(View.INVISIBLE);
                        }
                    });
                    return false;
                }
            });
            clockLayout.addView(lineclock);
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
