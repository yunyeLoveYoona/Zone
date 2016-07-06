package com.yun.zone;

import android.app.Application;

import com.yun.zone.core.Zone;

/**
 * Created by dell on 2016/7/6.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Zone.init(this);
    }
}
