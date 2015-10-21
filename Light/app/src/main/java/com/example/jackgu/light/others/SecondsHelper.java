package com.example.jackgu.light.others;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/7.
 */
public class SecondsHelper {
    HashMap<String,String> seconds = new HashMap<String, String>();
    public SecondsHelper(){
        seconds.put("0.5","05");
        seconds.put("1","0A");
        seconds.put("2","14");
        seconds.put("3","1E");
        seconds.put("4","28");
        seconds.put("5","32");
        seconds.put("6","3C");
        seconds.put("7","46");
        seconds.put("8","50");
        seconds.put("9","5A");
        seconds.put("10","64");
    }

    public String Get16String(String tennumber){
        return seconds.get(tennumber);
    }
}
