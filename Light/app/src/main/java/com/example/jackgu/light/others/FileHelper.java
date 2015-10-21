package com.example.jackgu.light.others;

import com.example.jackgu.light.model.ClockModel;
import com.example.jackgu.light.model.ColorModel;
import com.example.jackgu.light.model.ModeModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by jackgu on 6/9/2015.
 */
public class FileHelper {

    public static int AddMode(String path, ModeModel mode) {
        int no = -1;
        try {
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            if (modes.size() == 6) {
                int earlyestMode = GetEarlyestModeRecord(modes);
                no = earlyestMode;
                mode.setNo(earlyestMode);
                modes.set(earlyestMode, mode);
            } else {
                mode.setNo(modes.size());
                no = modes.size();
                modes.add(mode);
            }

            ReWriteModeFile(path, modes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }

    public static void AddModeSetting(String path, ModeModel mode) {
        try {
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            boolean isexist = false;
            for (int i = 0; i <modes.size() ; i++) {
                if(modes.get(i).getMode().equals(mode.getMode()))
                {
                    modes.remove(i);
                    modes.add(i,mode);
                    isexist = true;
                }
            }

            if(!isexist){
                modes.add(mode);
            }

            ReWriteModeFile(path, modes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ReWriteModeFile(String path,ArrayList<ModeModel> modes){
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < modes.size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(modes.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ModeModel GetModeSetting(String path, String modeName) {
        try {
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            for (int i = 0; i <modes.size() ; i++) {
                if(modes.get(i).getMode().equals(modeName))
                {
                    return modes.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void FavorModeSetting(String path, String modeName, boolean isfavor, int favorIndex) {
        try {
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            for (int i = 0; i <modes.size() ; i++) {
                if(modes.get(i).getMode().equals(modeName))
                {
                    modes.get(i).setNo(favorIndex);
                    modes.get(i).setIsFavorite(isfavor);
                }
            }
            ReWriteModeFile(path,modes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RemoveMode(String path, ModeModel mode) {
        try {
            String clockPath = path.replace("favorite_mode.json", "favorite_clock.json");
            String clockSettingPath = path.replace("favorite_mode.json", "clock_setting.json");
            ArrayList<ClockModel> clockModels = GetAllClockRecord(clockPath);
            ClockModel clockSetting = GetClockSetting(clockSettingPath);
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            Gson gson = new Gson();
            for (int i=0;i<modes.size();i++){
                if(modes.get(i).getMode().equals(mode.getMode())
                        &&gson.toJson(modes.get(i).getColors()).equals(gson.toJson(mode.getColors()))
                        &&gson.toJson(modes.get(i).getSeconds()).equals(gson.toJson(mode.getSeconds())))
                {
                    modes.remove(modes.get(i));
                    Iterator<ClockModel> iterator = clockModels.iterator();
                    while (iterator.hasNext()) {
                        ClockModel clock = iterator.next(); // must be called before you can call i.remove()
                        if(clock.getModeIndex() ==i) {
                            iterator.remove();
                        }
                    }
                    if(clockSetting.getModeIndex() == i){
                        File clockSettingFile = new File(clockSettingPath);
                        clockSettingFile.delete();
                    }
                    break;
                }
            }

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < modes.size(); i++) {
                String json = gson.toJson(modes.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();

            File clockfile = new File(clockPath);
            if (!clockfile.exists()) {
                clockfile.createNewFile();
            }
            BufferedWriter clockout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(clockfile, false)));
            for (int i = 0; i < clockModels.size(); i++) {
                String json = gson.toJson(clockModels.get(i));
                clockout.write(json);
                clockout.newLine();
            }
            clockout.flush();
            clockout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModeModel GetMode(String path, int id) {
        try {
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            return modes.get(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void RemoveMode(String path, int id) {
        try {
            String clockPath = path.replace("favorite_mode.json", "favorite_clock.json");
            String modesettingPath = path.replace("favorite_mode.json", "mode_setting.json");
            String clocksettingPath = path.replace("favorite_mode.json", "clock_setting.json");
            ArrayList<ClockModel> clockModels = GetAllClockRecord(clockPath);
            ArrayList<ModeModel> modes = GetAllModeRecord(path);
            ArrayList<ModeModel> modesettings = GetAllModeRecord(modesettingPath);
            Gson gson = new Gson();
            if(modes.size()>id) {
                modes.remove(id);
            }
            Iterator<ClockModel> iterator = clockModels.iterator();
            while (iterator.hasNext()) {
                ClockModel clock = iterator.next(); // must be called before you can call i.remove()
                if(clock.getModeIndex() ==id) {
                    iterator.remove();
                }
            }
            for (int i = 0; i < modesettings.size(); i++) {
                if(modesettings.get(i).getNo() == id)
                {
                    modesettings.get(i).setIsFavorite(false);
                    break;
                }
            }
            ReWriteModeFile(path, modes);
            ReWriteModeFile(modesettingPath, modesettings);
            ReWriteClockFile(clockPath, clockModels);
            ReWriteClockFile(clocksettingPath, new ArrayList<ClockModel>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void AddColor(String path, ColorModel colorModel) {
        try {
            ArrayList<ColorModel> colors = GetAllColorRecord(path);
            if (colors.size() == 5) {
                int earlyestColor = GetEarlyestColorRecord(colors);
                colors.set(earlyestColor, colorModel);
            } else {
                colors.add(colorModel);
            }

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < colors.size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(colors.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RemoveColor(String path, ColorModel colorModel) {
        try {
            ArrayList<ColorModel> colors = GetAllColorRecord(path);
            Gson gson = new Gson();
            for (ColorModel color : colors) {
                if(colorModel.getColor().equals(color.getColor()) && colorModel.getShadow() == color.getShadow())
                {
                    colors.remove(color);
                    break;
                }
            }

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < colors.size(); i++) {
                String json = gson.toJson(colors.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RemoveColor(String path, int id) {
        try {
            ArrayList<ColorModel> colors = GetAllColorRecord(path);
            Gson gson = new Gson();
            colors.remove(id);

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < colors.size(); i++) {
                String json = gson.toJson(colors.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ColorModel GetColor(String path, int id) {
            ArrayList<ColorModel> colors = GetAllColorRecord(path);
            return colors.get(id);
    }

    public static ClockModel GetClock(String path, int id) {
        ArrayList<ClockModel> clocks = GetAllClockRecord(path);
        return clocks.get(id);
    }

    public static void AddClock(String path, ClockModel clockModel) {
        try {
            ArrayList<ClockModel> clocks = GetAllClockRecord(path);
            if (clocks.size() == 2) {
                int earlyestClock = GetEarlyestClockRecord(clocks);
                clocks.set(earlyestClock, clockModel);
            } else {
                clocks.add(clockModel);
            }

            ReWriteClockFile(path,clocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void AddClockSetting(String path, ClockModel clockModel) {
        try {
            ArrayList<ClockModel> clocks = new ArrayList<ClockModel>();
            clocks.add(clockModel);

            ReWriteClockFile(path, clocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ReWriteClockFile(String path , ArrayList<ClockModel> clocks)
    {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < clocks.size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(clocks.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void RemoveClock(String path, ClockModel clockModel) {
        try {
            ArrayList<ClockModel> clocks = GetAllClockRecord(path);
            Gson gson = new Gson();

            for(ClockModel clock: clocks){
                if(clock.getOpenTime().equals(clockModel.getOpenTime())
                        &&clock.getCloseTime().equals(clockModel.getCloseTime())
                        &&clock.getModeIndex() == clockModel.getModeIndex()){
                    clocks.remove(clock);
                    break;
                }
            }

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            for (int i = 0; i < clocks.size(); i++) {
                String json = gson.toJson(clocks.get(i));
                out.write(json);
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RemoveClock(String path, int id) {
        try {
            String clocksettingPath = path.replace("favorite_clock.json", "clock_setting.json");
            ClockModel clockSetting = GetClockSetting(clocksettingPath);
            ArrayList<ClockModel> clocks = GetAllClockRecord(path);
            Gson gson = new Gson();
            if(clockSetting.getModeName().equals(clocks.get(id).getModeName())
                   &&clockSetting.getOpenTime().equals(clocks.get(id).getOpenTime())
                    &&clockSetting.getCloseTime().equals(clocks.get(id).getCloseTime())){
                clockSetting.setIsFavorite(false);
            }
            clocks.remove(id);

            ReWriteClockFile(path,clocks);
            ArrayList<ClockModel> clockSettings = new ArrayList<ClockModel>();
            clockSettings.add(clockSetting);
            ReWriteClockFile(clocksettingPath, clockSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ClockModel GetClockSetting(String path) {
        try {
            ArrayList<ClockModel> modes = GetAllClockRecord(path);
            if(modes.size()>0){
                return modes.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int GetEarlyestModeRecord(ArrayList<ModeModel> modes) {
        Date date = new Date();
        int minRecord = 0;
        for (int i = 0; i < modes.size(); i++) {
            ModeModel mode = modes.get(i);
            if (mode.getDatetime().before(date)) {
                date = mode.getDatetime();
                minRecord = i;
            }
        }
        return minRecord;
    }

    public static int GetEarlyestColorRecord(ArrayList<ColorModel> colors) {
        Date date = new Date();
        int minRecord = 0;
        for (int i = 0; i < colors.size(); i++) {
            ColorModel color = colors.get(i);
            if (color.getDatetime().before(date)) {
                date = color.getDatetime();
                minRecord = i;
            }
        }
        return minRecord;
    }

    public static int GetEarlyestClockRecord(ArrayList<ClockModel> clocks) {
        Date date = new Date();
        int minRecord = 0;
        for (int i = 0; i < clocks.size(); i++) {
            ClockModel clock = clocks.get(i);
            if (clock.getDatetime().before(date)) {
                date = clock.getDatetime();
                minRecord = i;
            }
        }
        return minRecord;
    }

    public static ArrayList<ColorModel> GetAllColorRecord(String path) {
        ArrayList<ColorModel> colors = new ArrayList<ColorModel>();
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                ColorModel color = gson.fromJson(line, ColorModel.class);
                colors.add(color);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colors;
    }

    public static ArrayList<ModeModel> GetAllModeRecord(String path) {
        ArrayList<ModeModel> modeModels = new ArrayList<ModeModel>();
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                ModeModel model = gson.fromJson(line, ModeModel.class);
                modeModels.add(model);
                line = br.readLine();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return modeModels;
    }

    public static ArrayList<ClockModel> GetAllClockRecord(String path) {
        ArrayList<ClockModel> modeModels = new ArrayList<ClockModel>();
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                ClockModel model = gson.fromJson(line, ClockModel.class);
                modeModels.add(model);
                line = br.readLine();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return modeModels;
    }
}
