package com.example.jackgu.light.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jackgu.light.R;
import com.example.jackgu.light.others.BluetoothHelper;
import com.example.jackgu.light.view.TitleView;

import java.util.ArrayList;

/**
 * @author yangyu
 *         功能描述：首页fragment页面
 */
public class HomeFragment extends Fragment {
    private View mParent;
    private FragmentActivity mActivity;
    private TitleView mTitle;
    private LinearLayout bluetooth_list_1;
    private LinearLayout bluetooth_list_2;
    private Switch switch1;
    private Switch switch2;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();

    private static final long SCAN_PERIOD = 10000;
    private String strPsw = "333333";
    private Handler mHandler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mGatt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = getActivity();
        mParent = view;
        mTitle = (TitleView) mParent.findViewById(R.id.home_title);
        mTitle.setTitle(R.string.title_home);
        bluetooth_list_1 = (LinearLayout) view.findViewById(R.id.bluetooth_list_1);
        bluetooth_list_2 = (LinearLayout) view.findViewById(R.id.bluetooth_list_2);
        mHandler = new Handler();

        BluetoothManager bluetoothManager =
                (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();

            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    //弹出对话框提示用户是后打开
                    Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enabler, 0);
                }
                BluetoothHelper.clearScanedDevices();
            }
        }

        radioButtons.clear();
        checkBoxes.clear();
        switch1 = (Switch) view.findViewById(R.id.switch1);
        switch2 = (Switch) view.findViewById(R.id.switch2);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2.toggle();
                BluetoothHelper.clearCheckedDevices();
                if(switch1.isChecked()){
                    EnableAllCheckBox();
                    DisableAllRadio();
                }else{
                    DisableAllCheckBox();
                    EnableAllRadio();
                }
            }
        });
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch1.toggle();
                BluetoothHelper.clearCheckedDevices();
                if(switch1.isChecked()){
                    EnableAllCheckBox();
                    DisableAllRadio();
                }else{
                    DisableAllCheckBox();
                    EnableAllRadio();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bluetoothAdapter != null) {
            scanLeDevice(true);
        }
    }

    private void EnableAllCheckBox(){
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setEnabled(true);
        }
    }

    private void DisableAllCheckBox(){
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setEnabled(false);
            checkBoxes.get(i).setChecked(false);
        }
    }

    private void EnableAllRadio(){
        for (int i = 0; i < radioButtons.size(); i++) {
            radioButtons.get(i).setEnabled(true);
        }
    }

    private void DisableAllRadio(){
        for (int i = 0; i < radioButtons.size(); i++) {
            radioButtons.get(i).setEnabled(false);
            radioButtons.get(i).setChecked(false);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    mActivity.runOnUiThread(new Runnable() {
                        //@Override
                        public void run() {
                            ArrayList<BluetoothDevice> devices = BluetoothHelper.getScanedDevices();
                            if (!devices.contains(device)) {
                                BluetoothHelper.addScanedDevice(device);
                                BluetoothDevice de = device;
                                RelativeLayout deviceLine1 = new RelativeLayout(mActivity);
                                RelativeLayout deviceLine2 = new RelativeLayout(mActivity);
                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                                deviceLine1.setLayoutParams(layoutParams);
                                deviceLine2.setLayoutParams(layoutParams);
                                deviceLine1.setBackgroundResource(R.drawable.border);

                                TextView textView = new TextView(mActivity);
                                RelativeLayout.LayoutParams textlayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textlayoutParams.leftMargin = 30;
                                textlayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                                textView.setLayoutParams(textlayoutParams);
                                textView.setText(de.getName());
                                textView.setTag(de.getAddress());
                                deviceLine1.addView(textView);

                                TextView radiotextView = new TextView(mActivity);
                                RelativeLayout.LayoutParams radiotextlayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                radiotextlayoutParams.leftMargin = 30;
                                radiotextlayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                                radiotextView.setLayoutParams(textlayoutParams);
                                radiotextView.setText(de.getName());
                                radiotextView.setTag(de.getAddress());
                                deviceLine2.addView(radiotextView);

                                CheckBox checkbox = new CheckBox(mActivity);
                                RelativeLayout.LayoutParams checklayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                checklayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                checkbox.setLayoutParams(checklayoutParams);
                                checkbox.setTag(de.getAddress());
                                if (switch1.isChecked()) {
                                    checkbox.setEnabled(true);
                                } else {
                                    checkbox.setEnabled(false);
                                }
                                deviceLine1.addView(checkbox);
                                checkBoxes.add(checkbox);
                                bluetooth_list_1.addView(deviceLine1);
                                checkbox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //bind
                                        CheckBox checkBox = (CheckBox) v;
                                        if (checkBox.isChecked()) {
                                            String deviceAddress = (String) checkBox.getTag();
                                            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                                            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                                                //boolean setpinresult = BluetoothHelper.setPin(device.getClass(), device, strPsw);
                                                //System.out.println("setpin:"+setpinresult);
                                                boolean bondresult = BluetoothHelper.createBond(device.getClass(), device);
                                                //System.out.println("bondresult:"+bondresult);
//                                                if (bondresult) {
//                                                    boolean cancelresult = BluetoothHelper.cancelPairingUserInput(device.getClass(), device);
//                                                    System.out.println("cancelresult:"+cancelresult);
//                                                } else {
//                                                    //make user input
//                                                }
                                            }
                                            BluetoothHelper.addCheckedDevice(device);
                                        }
                                    }
                                });

                                RadioButton radioButton = new RadioButton(mActivity);
                                RelativeLayout.LayoutParams radiolayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                radiolayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                radioButton.setLayoutParams(radiolayoutParams);
                                radioButton.setTag(de.getAddress());
                                if (switch2.isChecked()) {
                                    radioButton.setEnabled(true);
                                } else {
                                    radioButton.setEnabled(false);
                                }
                                deviceLine2.addView(radioButton);
                                radioButtons.add(radioButton);
                                bluetooth_list_2.addView(deviceLine2);
                                radioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RadioButton radioButton = (RadioButton) v;
                                        if (!radioButton.isChecked()) {
                                            String deviceAddress = (String) v.getTag();
                                            for (int i = 0; i < radioButtons.size(); i++) {
                                                if (!radioButtons.get(i).getTag().equals(deviceAddress)) {
                                                    radioButtons.get(i).setChecked(false);
                                                }
                                            }
                                            BluetoothHelper.clearCheckedDevices();
                                            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                                            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                                                boolean bondresult = BluetoothHelper.createBond(device.getClass(), device);
                                            }
                                            BluetoothHelper.addCheckedDevice(device);
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            };

    @Override
    public void onPause() {
        super.onPause();
        if (bluetoothAdapter != null) {
            scanLeDevice(false);
        }
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