package com.example.jackgu.light;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.example.jackgu.light.fragment.FavoritesFragment;
import com.example.jackgu.light.fragment.FragmentIndicator;
import com.example.jackgu.light.fragment.FragmentIndicator.OnIndicateListener;
import com.example.jackgu.light.others.BluetoothHelper;
import com.example.jackgu.light.others.BluetoothLeService;
import com.example.jackgu.light.others.CommandBuilder;
import com.example.jackgu.light.others.FragmentHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author yangyu
 *  功能描述：主Activity类，继承自FragmentActivity 
 */
public class MainActivity extends FragmentActivity {

    public static Fragment[] mFragments;

    private Fragment mainFragment;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        manager =  getSupportFragmentManager();
        mainFragment  = manager.findFragmentById(R.id.fragment_main);

        setFragmentIndicator(0);
        FragmentHelper.ShowFragemnt(getSupportFragmentManager(), "com.example.jackgu.light.fragment.MainFragment");
        FragmentIndicator.setIndicator(4);
        getWindow().findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.ShowFragemnt(getSupportFragmentManager(), "com.example.jackgu.light.fragment.MainFragment");
                FragmentIndicator.setIndicator(4);

            }
        });
        //startBleService();
    }

    /**
     * 初始化fragment 
     */
    private void setFragmentIndicator(int whichIsDefault) {
        mFragments = new Fragment[4];
        mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
        mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_favorites);
        mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_settings);
        mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_info);
        FragmentHelper.HideAllFragemnt(getSupportFragmentManager());
        FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
        FragmentIndicator.setIndicator(whichIsDefault);
        mIndicator.setOnIndicateListener(new OnIndicateListener() {
            @Override
            public void onIndicate(View v, int which) {
                FragmentHelper.HideAllFragemnt(getSupportFragmentManager());
                if(which == 1){
                    FragmentHelper.RecreateFragemnt(getSupportFragmentManager(), "com.example.jackgu.light.fragment.FavoritesFragment", R.id.fragment_container, "FavoritesFragment");
                }
                else {
                    getSupportFragmentManager().beginTransaction().show(mFragments[which]).commit();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().show(mFragments[whichIsDefault]).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private BluetoothLeService mBluetoothLeService;
    private final static String TAG = "MainActivity";
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public void startBleService(){
        Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
        MainActivity.this.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        //unregisterReceiver(mGattUpdateReceiver);
        //mBluetoothLeService = null;
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                BluetoothGattService service = BluetoothLeService.getBtGatt().getService(UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                String comandType = BluetoothHelper.getCommandType();
                if(!comandType.equals("Alarm")) {
                    BluetoothGattCharacteristic openChar = service.getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                    openChar.setValue(BluetoothHelper.getCommands().get("Open"));
                    boolean ok = BluetoothLeService.getInstance().writeCharacteristic(openChar);
                    if (ok)
                        ok = BluetoothLeService.getInstance().waitIdle(1000);
                    BluetoothGattCharacteristic colorChar = service.getCharacteristic(UUID.fromString("0000fff5-0000-1000-8000-00805f9b34fb"));

                    if (comandType.equals("Color") || comandType.equals("Monochrome") || comandType.equals("Custom")) {
                        colorChar.setValue(BluetoothHelper.getCommands().get("Color"));
                        boolean colorok = BluetoothLeService.getInstance().writeCharacteristic(colorChar);
                        if (colorok)
                            colorok = BluetoothLeService.getInstance().waitIdle(1000);
                    }
                    if (comandType.equals("Lighting") || comandType.equals("FireCracker") || comandType.equals("Gradual_Change") || comandType.equals("Hopping")) {
                        colorChar.setValue(BluetoothHelper.getCommands().get("Color1"));
                        boolean color1ok = BluetoothLeService.getInstance().writeCharacteristic(colorChar);
                        if (color1ok)
                            color1ok = BluetoothLeService.getInstance().waitIdle(1000);
                        colorChar.setValue(BluetoothHelper.getCommands().get("Color2"));
                        boolean color2ok = BluetoothLeService.getInstance().writeCharacteristic(colorChar);
                        if (color2ok)
                            color2ok = BluetoothLeService.getInstance().waitIdle(1000);
                    }
                    BluetoothGattCharacteristic modeChar = service.getCharacteristic(UUID.fromString("0000fff9-0000-1000-8000-00805f9b34fb"));
                    modeChar.setValue(BluetoothHelper.getCommands().get("Mode"));
                    boolean modeok = BluetoothLeService.getInstance().writeCharacteristic(modeChar);
                    if (modeok)
                        modeok = BluetoothLeService.getInstance().waitIdle(1000);
                }else{
                    /*BluetoothGattCharacteristic clockChar = service.getCharacteristic(UUID.fromString("0000fff8-0000-1000-8000-00805f9b34fb"));
                    HashMap<String,byte[]> commands =  BluetoothHelper.getCommands();
                    Iterator iter = commands.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        Object val = entry.getValue();
                        clockChar.setValue((byte[])val);
                        boolean modeok = BluetoothLeService.getInstance().writeCharacteristic(clockChar);
                        if (modeok)
                            modeok = BluetoothLeService.getInstance().waitIdle(1000);
                    }*/
                }
            } else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
            else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
}