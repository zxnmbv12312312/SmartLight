package com.example.jackgu.light.others;

import android.content.res.Resources;

import com.example.jackgu.light.model.ColorModel;
import com.example.jackgu.light.model.ModeModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class CommandBuilder {
    public  static SecondsHelper  secondsHelper = new SecondsHelper();

    public static HashMap<String,byte[]> BuildColorCommand(ColorModel colorModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();

        int resourceStrId = resources.getIdentifier(colorModel.getColor(), "color", "com.example.jackgu.light");
        String rgbvalue = resources.getString(resourceStrId);
        String color = FormateColor(rgbvalue);
        String command = "00110A" + color.toUpperCase();
        byte[] bytergb = BluetoothHelper.hexStringToByte(command);
        int brightness = colorModel.getShadow();
        bytergb[3] = (byte) ((((bytergb[3] & 0xFF) & 0xFF) * (brightness + 1) * 10 / 100) & 0xff);
        bytergb[4] = (byte) ((((bytergb[4] & 0xFF) & 0xFF) * (brightness + 1) * 10 / 100) & 0xff);
        bytergb[5] = (byte) ((((bytergb[5] & 0xFF) & 0xFF) * (brightness + 1) * 10 / 100) & 0xff);

        commands.put("Open", BluetoothHelper.hexStringToByte("01"));
        commands.put("Color",bytergb);
        commands.put("Mode",BluetoothHelper.hexStringToByte("000A"));

        return commands;
    }

    public static HashMap<String,byte[]> BuildLightCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==4) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
            int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
            int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
            String rgbvalue0 = resources.getString(resourceStrId0);
            String rgbvalue1 = resources.getString(resourceStrId1);
            String rgbvalue2 = resources.getString(resourceStrId2);
            String rgbvalue3 = resources.getString(resourceStrId3);
            String color0=FormateColor(rgbvalue0);
            String color1=FormateColor(rgbvalue1);
            String color2=FormateColor(rgbvalue2);
            String color3=FormateColor(rgbvalue3);
            String command1 = String.format("0081%s%s02000000%s%s02000000",
                    secondsHelper.Get16String(seconds.get(0)),color0,
                    secondsHelper.Get16String(seconds.get(1)),color1);
            String command2 =  String.format("0082%s%s02000000%s%s02000000",
                    secondsHelper.Get16String(seconds.get(2)),color2,
                    secondsHelper.Get16String(seconds.get(3)),color3);
            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color1",BluetoothHelper.hexStringToByte(command1));
            commands.put("Color2", BluetoothHelper.hexStringToByte(command2));
            commands.put("Mode",BluetoothHelper.hexStringToByte("00AA"));
        }
        return commands;
    }


    public static HashMap<String,byte[]> BuildMonochromeCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==1) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            String rgbvalue0 = resources.getString(resourceStrId0);
            String color0=FormateColor(rgbvalue0);
            String command1 = String.format("0021%s%s%s000000",
                    secondsHelper.Get16String(seconds.get(0)),color0,secondsHelper.Get16String(seconds.get(0)));

            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color",BluetoothHelper.hexStringToByte(command1));
            commands.put("Mode",BluetoothHelper.hexStringToByte("0002"));
        }
        return commands;
    }

    public static HashMap<String,byte[]> BuildFireCrackerCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==4) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
            int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
            int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
            String rgbvalue0 = resources.getString(resourceStrId0);
            String rgbvalue1 = resources.getString(resourceStrId1);
            String rgbvalue2 = resources.getString(resourceStrId2);
            String rgbvalue3 = resources.getString(resourceStrId3);
            String color0=FormateColor(rgbvalue0);
            String color1=FormateColor(rgbvalue1);
            String color2=FormateColor(rgbvalue2);
            String color3=FormateColor(rgbvalue3);
            String command1 = String.format("0081%s%s02000000%s%s02000000",
                    secondsHelper.Get16String(seconds.get(0)), color0,
                    secondsHelper.Get16String(seconds.get(1)), color1);
            String command2 =  String.format("0082%s%s02000000%s%s02000000",
                    secondsHelper.Get16String(seconds.get(2)),color2,
                    secondsHelper.Get16String(seconds.get(3)),color3);
            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color1",BluetoothHelper.hexStringToByte(command1));
            commands.put("Color2", BluetoothHelper.hexStringToByte(command2));
            commands.put("Mode",BluetoothHelper.hexStringToByte("0014"));
        }
        return commands;
    }

    public static HashMap<String,byte[]> BuildGradualChangeCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==1) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
            int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
            int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
            int resourceStrId4 = resources.getIdentifier(colors.get(4), "color", "com.example.jackgu.light");
            int resourceStrId5 = resources.getIdentifier(colors.get(5), "color", "com.example.jackgu.light");
            int resourceStrId6 = resources.getIdentifier(colors.get(6), "color", "com.example.jackgu.light");
            String rgbvalue0 = resources.getString(resourceStrId0);
            String rgbvalue1 = resources.getString(resourceStrId1);
            String rgbvalue2 = resources.getString(resourceStrId2);
            String rgbvalue3 = resources.getString(resourceStrId3);
            String rgbvalue4 = resources.getString(resourceStrId4);
            String rgbvalue5 = resources.getString(resourceStrId5);
            String rgbvalue6 = resources.getString(resourceStrId6);
            String color0=FormateColor(rgbvalue0);
            String color1=FormateColor(rgbvalue1);
            String color2=FormateColor(rgbvalue2);
            String color3=FormateColor(rgbvalue3);
            String color4=FormateColor(rgbvalue4);
            String color5=FormateColor(rgbvalue5);
            String color6=FormateColor(rgbvalue6);
            String second = secondsHelper.Get16String(seconds.get(0));
            String command1 = String.format("0071%s%s%s%s%s%s%s%s",
                    second, color0,
                    second, color1,
                    second, color2,
                    second, color3);
            String command2 =  String.format("0072%s%s%s%s%s%s",
                    second,color4,
                    second,color5,
                    second,color6);
            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color1",BluetoothHelper.hexStringToByte(command1));
            commands.put("Color2", BluetoothHelper.hexStringToByte(command2));
            commands.put("Mode",BluetoothHelper.hexStringToByte("0002"));
        }
        return commands;
    }

    public static HashMap<String,byte[]> BuildHoppingCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==1) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
            int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
            int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
            int resourceStrId4 = resources.getIdentifier(colors.get(4), "color", "com.example.jackgu.light");
            int resourceStrId5 = resources.getIdentifier(colors.get(5), "color", "com.example.jackgu.light");
            int resourceStrId6 = resources.getIdentifier(colors.get(6), "color", "com.example.jackgu.light");
            String rgbvalue0 = resources.getString(resourceStrId0);
            String rgbvalue1 = resources.getString(resourceStrId1);
            String rgbvalue2 = resources.getString(resourceStrId2);
            String rgbvalue3 = resources.getString(resourceStrId3);
            String rgbvalue4 = resources.getString(resourceStrId4);
            String rgbvalue5 = resources.getString(resourceStrId5);
            String rgbvalue6 = resources.getString(resourceStrId6);
            String color0=FormateColor(rgbvalue0);
            String color1=FormateColor(rgbvalue1);
            String color2=FormateColor(rgbvalue2);
            String color3=FormateColor(rgbvalue3);
            String color4=FormateColor(rgbvalue4);
            String color5=FormateColor(rgbvalue5);
            String color6=FormateColor(rgbvalue6);
            String second = secondsHelper.Get16String(seconds.get(0));
            String command1 = String.format("0071%s%s%s%s%s%s%s%s",
                    second, color0,
                    second, color1,
                    second, color2,
                    second, color3);
            String command2 =  String.format("0072%s%s%s%s%s%s",
                    second,color4,
                    second,color5,
                    second,color6);
            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color1",BluetoothHelper.hexStringToByte(command1));
            commands.put("Color2", BluetoothHelper.hexStringToByte(command2));
            commands.put("Mode",BluetoothHelper.hexStringToByte("000A"));
        }
        return commands;
    }

    public static HashMap<String,byte[]> BuildCustomCommand(ModeModel modeModel,Resources resources){
        HashMap<String,byte[]> commands = new HashMap<String,byte[]>();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        if(seconds.size()==3) {
            int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
            int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
            int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");

            String rgbvalue0 = resources.getString(resourceStrId0);
            String rgbvalue1 = resources.getString(resourceStrId1);
            String rgbvalue2 = resources.getString(resourceStrId2);

            String color0=FormateColor(rgbvalue0);
            String color1=FormateColor(rgbvalue1);
            String color2=FormateColor(rgbvalue2);

            String command1 = String.format("0031%s%s%s%s%s%s",
                    secondsHelper.Get16String(seconds.get(0)), color0,
                    secondsHelper.Get16String(seconds.get(1)), color1,
                    secondsHelper.Get16String(seconds.get(2)), color2);

            commands.put("Open", BluetoothHelper.hexStringToByte("01"));
            commands.put("Color",BluetoothHelper.hexStringToByte(command1));
            commands.put("Mode",BluetoothHelper.hexStringToByte("000A"));
        }
        return commands;
    }

    public static HashMap<String,byte[]>  BuildAlarmCommand(int clockNum, int hour, int minute,ModeModel modeModel, Resources resources){
        HashMap<String,byte[]>  commands = new HashMap<String,byte[]> ();
        Date date = new Date();
        String hourhexstr = intToHex(date.getHours());
        String minutehexstr = intToHex(date.getMinutes());
        String alarmhourhexstr = intToHex(hour);
        String alarmminutehexstr = intToHex(minute);;
        String resetSystime = String.format("00000A0B%s%s",hourhexstr,minutehexstr);
        String setAlarm = String.format("0%s010A0B%s%s", clockNum, alarmhourhexstr, alarmminutehexstr);
        commands.put("Reset", BluetoothHelper.hexStringToByte(resetSystime));
        commands.put("SetAlarm",BluetoothHelper.hexStringToByte(setAlarm));

        ArrayList<String> modeWay = getCommandModeByModeModel(modeModel,resources);
        for (int i = 0; i <modeWay.size() ; i++) {
            String storeMode = String.format("0%s02%s",clockNum,modeWay.get(i));
            commands.put("StoreMode"+i,BluetoothHelper.hexStringToByte(storeMode));
        }
        return commands;
    }

    public static String FormateColor(String rgbvalue){
        if (rgbvalue.length() == 9) {
            rgbvalue = rgbvalue.replace("#ff", "").toUpperCase();
        } else {
            rgbvalue = rgbvalue.replace("#", "").toUpperCase();
        }
        return rgbvalue;
    }


    private static  String intToHex(int number){
        String numberhex = Integer.toHexString(number);
        String numberhexstr = numberhex.length() ==2?numberhex:"0"+numberhex;
        return numberhexstr;
    }

    private static ArrayList<String> getCommandModeByModeModel(ModeModel modeModel,Resources resources){
        String mode = modeModel.getMode();
        ArrayList<String> seconds = modeModel.getSeconds();
        ArrayList<String> colors = modeModel.getColors();
        ArrayList<String> list =new ArrayList<String>();
        if(mode.equals("Lighting")) {
            if(seconds.size()==4) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
                int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
                int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
                String rgbvalue0 = resources.getString(resourceStrId0);
                String rgbvalue1 = resources.getString(resourceStrId1);
                String rgbvalue2 = resources.getString(resourceStrId2);
                String rgbvalue3 = resources.getString(resourceStrId3);
                String color0 = FormateColor(rgbvalue0);
                String color1 = FormateColor(rgbvalue1);
                String color2 = FormateColor(rgbvalue2);
                String color3 = FormateColor(rgbvalue3);
                String command1 = String.format("AA81%s%s02000000%s%s02000000",
                        secondsHelper.Get16String(seconds.get(0)), color0,
                        secondsHelper.Get16String(seconds.get(1)), color1);
                String command2 =  String.format("AA82%s%s02000000%s%s02000000",
                        secondsHelper.Get16String(seconds.get(2)),color2,
                        secondsHelper.Get16String(seconds.get(3)),color3);
                list.add(command1);
                list.add(command2);
            }
        }
        if(mode.equals("Monochrome")) {
            if(seconds.size()==1) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                String rgbvalue0 = resources.getString(resourceStrId0);
                String color0=FormateColor(rgbvalue0);
                String command1 = String.format("0221%s%s%s000000",
                        secondsHelper.Get16String(seconds.get(0)),color0,secondsHelper.Get16String(seconds.get(0)));
                list.add(command1);
            }
        }
        if(mode.equals("FireCracker")) {
            if(seconds.size()==4) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
                int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
                int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
                String rgbvalue0 = resources.getString(resourceStrId0);
                String rgbvalue1 = resources.getString(resourceStrId1);
                String rgbvalue2 = resources.getString(resourceStrId2);
                String rgbvalue3 = resources.getString(resourceStrId3);
                String color0=FormateColor(rgbvalue0);
                String color1=FormateColor(rgbvalue1);
                String color2=FormateColor(rgbvalue2);
                String color3=FormateColor(rgbvalue3);
                String command1 = String.format("1481%s%s02000000%s%s02000000",
                        secondsHelper.Get16String(seconds.get(0)), color0,
                        secondsHelper.Get16String(seconds.get(1)), color1);
                String command2 =  String.format("1482%s%s02000000%s%s02000000",
                        secondsHelper.Get16String(seconds.get(2)),color2,
                        secondsHelper.Get16String(seconds.get(3)),color3);
                list.add(command1);
                list.add(command2);
            }
        }
        if(mode.equals("Gradual_Change")) {
            if(seconds.size()==1) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
                int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
                int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
                int resourceStrId4 = resources.getIdentifier(colors.get(4), "color", "com.example.jackgu.light");
                int resourceStrId5 = resources.getIdentifier(colors.get(5), "color", "com.example.jackgu.light");
                int resourceStrId6 = resources.getIdentifier(colors.get(6), "color", "com.example.jackgu.light");
                String rgbvalue0 = resources.getString(resourceStrId0);
                String rgbvalue1 = resources.getString(resourceStrId1);
                String rgbvalue2 = resources.getString(resourceStrId2);
                String rgbvalue3 = resources.getString(resourceStrId3);
                String rgbvalue4 = resources.getString(resourceStrId4);
                String rgbvalue5 = resources.getString(resourceStrId5);
                String rgbvalue6 = resources.getString(resourceStrId6);
                String color0=FormateColor(rgbvalue0);
                String color1=FormateColor(rgbvalue1);
                String color2=FormateColor(rgbvalue2);
                String color3=FormateColor(rgbvalue3);
                String color4=FormateColor(rgbvalue4);
                String color5=FormateColor(rgbvalue5);
                String color6=FormateColor(rgbvalue6);
                String second = secondsHelper.Get16String(seconds.get(0));
                String command1 = String.format("0271%s%s%s%s%s%s%s%s",
                        second, color0,
                        second, color1,
                        second, color2,
                        second, color3);
                String command2 =  String.format("0272%s%s%s%s%s%s",
                        second,color4,
                        second,color5,
                        second,color6);
                list.add(command1);
                list.add(command2);
            }
        }
        if(mode.equals("Hopping")) {
            if(seconds.size()==1) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
                int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");
                int resourceStrId3 = resources.getIdentifier(colors.get(3), "color", "com.example.jackgu.light");
                int resourceStrId4 = resources.getIdentifier(colors.get(4), "color", "com.example.jackgu.light");
                int resourceStrId5 = resources.getIdentifier(colors.get(5), "color", "com.example.jackgu.light");
                int resourceStrId6 = resources.getIdentifier(colors.get(6), "color", "com.example.jackgu.light");
                String rgbvalue0 = resources.getString(resourceStrId0);
                String rgbvalue1 = resources.getString(resourceStrId1);
                String rgbvalue2 = resources.getString(resourceStrId2);
                String rgbvalue3 = resources.getString(resourceStrId3);
                String rgbvalue4 = resources.getString(resourceStrId4);
                String rgbvalue5 = resources.getString(resourceStrId5);
                String rgbvalue6 = resources.getString(resourceStrId6);
                String color0=FormateColor(rgbvalue0);
                String color1=FormateColor(rgbvalue1);
                String color2=FormateColor(rgbvalue2);
                String color3=FormateColor(rgbvalue3);
                String color4=FormateColor(rgbvalue4);
                String color5=FormateColor(rgbvalue5);
                String color6=FormateColor(rgbvalue6);
                String second = secondsHelper.Get16String(seconds.get(0));
                String command1 = String.format("0A71%s%s%s%s%s%s%s%s",
                        second, color0,
                        second, color1,
                        second, color2,
                        second, color3);
                String command2 =  String.format("0A72%s%s%s%s%s%s",
                        second,color4,
                        second,color5,
                        second,color6);
                list.add(command1);
                list.add(command2);
            }
        }
        else {
            if(seconds.size()==3) {
                int resourceStrId0 = resources.getIdentifier(colors.get(0), "color", "com.example.jackgu.light");
                int resourceStrId1 = resources.getIdentifier(colors.get(1), "color", "com.example.jackgu.light");
                int resourceStrId2 = resources.getIdentifier(colors.get(2), "color", "com.example.jackgu.light");

                String rgbvalue0 = resources.getString(resourceStrId0);
                String rgbvalue1 = resources.getString(resourceStrId1);
                String rgbvalue2 = resources.getString(resourceStrId2);

                String color0=FormateColor(rgbvalue0);
                String color1=FormateColor(rgbvalue1);
                String color2=FormateColor(rgbvalue2);

                String command1 = String.format("0A31%s%s%s%s%s%s",
                        secondsHelper.Get16String(seconds.get(0)), color0,
                        secondsHelper.Get16String(seconds.get(1)), color1,
                        secondsHelper.Get16String(seconds.get(2)), color2);
                list.add(command1);
            }
        }
        return list;
    }
}
