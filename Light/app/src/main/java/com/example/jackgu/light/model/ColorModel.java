package com.example.jackgu.light.model;

import java.util.Date;

/**
 * Created by jackgu on 6/10/2015.
 */
public class ColorModel {
    private String color;
    public String getColor(){
        return this.color;
    }

    public void setColor(String color){
        this.color = color;
    }

    private int shadow = 0;

    public int getShadow(){
        return this.shadow;
    }

    public void setShadow(int shadow){
        this.shadow = shadow;
    }

    private Date datetime = new Date();

    public Date getDatetime(){
        return this.datetime;
    }

}
