package com.example.jackgu.light.model;

import com.example.jackgu.light.R;

import java.util.Date;

/**
 * Created by jackgu on 6/11/2015.
 */
public class ClockModel {

    private String openTime;

    public String getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    private String closeTime;

    public String getCloseTime() {
        return this.closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    private String modeName;

    public String getModeName() {
        return this.modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    private String modeText;

    public String getModeText() {
        return this.modeText;
    }

    public void setModeText(String modeText) {
        this.modeText = modeText;
    }

    private int modeIndex;

    public void setModeIndex(int modeIndex) {
        this.modeIndex = modeIndex;
    }

    public int getModeIndex() {
        return this.modeIndex;
    }

//    private ModeModel modeModel;
//
//    public ModeModel getModeModel() {
//        return this.modeModel;
//    }
//
//    public void setModeModel(ModeModel modeModel) {
//        this.modeModel = modeModel;
//    }

    private Date datetime = new Date();

    public Date getDatetime() {
        return this.datetime;
    }


    private boolean isfavorite = false;

    public void setIsFavorite(boolean isfavorite){
        this.isfavorite = isfavorite;
    }

    public boolean getIsFavorite(){
        return this.isfavorite;
    }
}
