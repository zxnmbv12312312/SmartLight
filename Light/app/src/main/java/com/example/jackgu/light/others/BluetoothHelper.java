package com.example.jackgu.light.others;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.res.Resources;

import com.example.jackgu.light.model.ClockModel;
import com.example.jackgu.light.model.ModeModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jackgu on 6/26/2015.
 */
public class BluetoothHelper {

    private static ArrayList<BluetoothDevice> scanedDeviceList = new ArrayList<BluetoothDevice>();
    private static ArrayList<BluetoothDevice> selectedCheckDeviceList = new ArrayList<BluetoothDevice>();
    private static HashMap<String,byte[]> command=new HashMap<String,byte[]>();
    private static String comandType = "Color";

    private static BluetoothGatt mGatt;
    private static String writeMode;
    //private static byte[] writeCommand;
    private static ArrayList<byte[]> writeCommandList = new ArrayList<byte[]>();
    private static HashMap<String,byte[]> writeModeCommandList = new HashMap<String,byte[]>();

    public static  ArrayList<BluetoothDevice> getScanedDevices(){
        return scanedDeviceList;
    }

    public static  void clearScanedDevices(){
        scanedDeviceList.clear();
    }

    public static  void addScanedDevice(BluetoothDevice device){
        if(scanedDeviceList.size()<=5) {
            boolean isexist = false;
            for (int i =0;i< scanedDeviceList.size();i++) {
                if(device.getAddress().equals( scanedDeviceList.get(i).getAddress())){
                    isexist = true;
                }

            }
            if(!isexist) {
                scanedDeviceList.add(device);
            }
        }
    }

    public static  ArrayList<BluetoothDevice> getCheckedDevices(){
        return selectedCheckDeviceList;
    }

    public static  void clearCheckedDevices(){
        selectedCheckDeviceList.clear();
    }

    public static  void addCheckedDevice(BluetoothDevice device){
        if(selectedCheckDeviceList.size()<=5) {
            boolean isexist = false;
            for (int i =0;i< selectedCheckDeviceList.size();i++) {
                if(device.getAddress().equals( selectedCheckDeviceList.get(i).getAddress())){
                    isexist = true;
                }

            }
            if(!isexist) {
                selectedCheckDeviceList.add(device);
            }
        }
    }

    public static  HashMap<String,byte[]> getCommands(){
        return command;
    }
    public static  void setCommands(HashMap<String,byte[]> commands){
        command = commands;
    }

    public static  String getCommandType(){
        return comandType;
    }

    public static  void setCommandType(String commandType){
        comandType = commandType;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    private static String mBluetoothDeviceAddress;



    /**
     * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean createBond(Class btClass, BluetoothDevice btDevice)
    {
        try {
            Method createBondMethod = btClass.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
            return returnValue.booleanValue();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception
    {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    static public boolean setPin(Class btClass, BluetoothDevice btDevice,
                                 String str)
    {
        try
        {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[]
                            {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {str.getBytes("UTF-8")});
            return returnValue;
            //Log.e("returnValue", "" + returnValue);
        }
        catch (SecurityException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    // ȡ���û�����
    static public boolean cancelPairingUserInput(Class btClass,
                                                 BluetoothDevice device)
    {
        try {
            Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
            // cancelBondProcess()
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            return returnValue.booleanValue();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    // ȡ�����
    static public boolean cancelBondProcess(Class btClass,
                                            BluetoothDevice device)

            throws Exception
    {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     *
     * @param clsShow
     */
    static public void printAllInform(Class clsShow)
    {
        try
        {
            // ȡ�����з���
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++)
            {
                //Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
            }
            // ȡ�����г���
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++)
            {
                //Log.e("Field name", allFields[i].getName());
            }
        }
        catch (SecurityException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ArrayList<byte[]> BuildClockCommind( ClockModel clockModel, ModeModel modeModel, Resources resources)
    {
        ArrayList<byte[]> commands = new ArrayList<byte[]>();
        String clockcommand = "00010C1F";
        String[] openTime = clockModel.getOpenTime().split(":");
        if(openTime.length==2) {
            int hour = Integer.parseInt(openTime[0]);
            int minute = Integer.parseInt(openTime[1]);
            String hourhex= Integer.toHexString(hour);
            String minutehex= Integer.toHexString(minute);
            clockcommand =clockcommand+hourhex+minutehex;
        }
        commands.add(hexStringToByte(clockcommand));
        ArrayList<byte[]> colorCommand = BuildColorModeCommind(modeModel,resources);
        for (int i = 0; i < colorCommand.size(); i++) {
            commands.add(colorCommand.get(i));
        }
        return commands;
    }

    public static ArrayList<byte[]> BuildColorModeCommind(ModeModel modeModel, Resources resources) {

        ArrayList<byte[]> commands = new ArrayList<byte[]>();
        ArrayList<String> colors = modeModel.getColors();
        ArrayList<String> seconds = modeModel.getSeconds();

        String command1 =  colors.size()<=4?"0011":"0021";
        String command2 =  "0022";
        for (int i = 0; i < colors.size(); i++) {
            int resourceStrId = resources.getIdentifier(colors.get(i), "color", "com.example.jackgu.light");
            String rgbvalue = resources.getString(resourceStrId);
            if(rgbvalue.length()==9){
                rgbvalue = rgbvalue.replace("#ff","").toUpperCase();
            }else {
                rgbvalue = rgbvalue.replace("#", "").toUpperCase();
            }
            String second = seconds.size()>i?seconds.get(i):seconds.get(0);
            if(second.contains("."))second="1";
            if(i<=3) {
                command1 = command1 + String.format("%02d", second) + rgbvalue;
            }else{
                command2 = command2 + String.format("%02d", second) + rgbvalue;
            }
        }
        ArrayList<byte[]> commandList = new ArrayList<byte[]>();
        byte[] bytecommand = BluetoothHelper.hexStringToByte(command1);
        for (int i=0;i<bytecommand.length;i++){
            //i%4 not time byte
            if(i>1&&i%4!=2){
                bytecommand[i]=(byte)((((bytecommand[i]&0xFF)&0xFF)*100/100)&0xff);
            }
        }
        commandList.add(bytecommand);
        if(colors.size()>4){
            byte[] bytecommand2 = BluetoothHelper.hexStringToByte(command2);
            for (int i=0;i<bytecommand2.length;i++){
                //i%4 not time byte
                if(i>1&&i%4!=2){
                    bytecommand2[i]=(byte)((((bytecommand2[i]&0xFF)&0xFF)*100/100)&0xff);
                }
            }
            commandList.add(bytecommand2);
        }
        return commands;
    }
}
