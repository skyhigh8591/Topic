package com.example.topic_control;

import android.app.Application;

public class GlobalVariable extends Application {

    private String webAddress = "http://192.168.1.110:8080/topic/";
    //private String webAddress = "http://192.168.58.116:8080/topic/";

    public String getWeb(){
        return this.webAddress;
    }
}
