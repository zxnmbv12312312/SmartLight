package com.example.jackgu.light.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jackgu on 6/8/2015.
 */
public class ModeModel {

    private int no = 0;

    public int getNo() {
        return this.no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    private String mode;

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private ArrayList<String> colors = new ArrayList<String>();

    public ArrayList<String> getColors() {
        return this.colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors ;
    }

    private ArrayList<Integer> colorswheelnumber = new ArrayList<Integer>();

    public ArrayList<Integer> getColorsWheelNumber() {
        return this.colorswheelnumber;
    }

    public void setColorsWheelNumber(ArrayList<Integer> colorswheelnumber) {
        this.colorswheelnumber = colorswheelnumber ;
    }

    public void addColor(String color) {
        colors.add(color);
    }

    public void addColor(String color, int index) {
        colors.add(color);
        colorswheelnumber.add(index);
    }

    private ArrayList<String> seconds= new ArrayList<String>();

    public void addSecond(String second) {
        seconds.add(second);
    }
    public void addSecond(String second, int index) {
        seconds.add(second);
        secondswheelnumber.add(index);
    }

    public ArrayList<String> getSeconds() {
         return this.seconds;
    }

    public void setSeconds(ArrayList<String>  seconds) {
        this.seconds = seconds;
    }

    private ArrayList<Integer> secondswheelnumber = new ArrayList<Integer>();

    public ArrayList<Integer> getSecondsWheelNumber() {
        return this.secondswheelnumber;
    }

    public void setSecondsWheelNumber(ArrayList<Integer> secondswheelnumber) {
        this.secondswheelnumber = secondswheelnumber ;
    }

    private Date datetime = new Date();

    public void setDatetime(Date datetime){
        this.datetime = datetime;
    }

    public Date getDatetime(){
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
