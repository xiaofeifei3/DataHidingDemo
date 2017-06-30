package com.chenqihong.datahidingdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by chenqihong on 2017/6/30.
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Fresco.initialize(this);
    }
}
