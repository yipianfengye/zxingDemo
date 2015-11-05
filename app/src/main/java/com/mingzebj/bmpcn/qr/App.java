package com.mingzebj.bmpcn.qr;

import android.app.Application;
import android.graphics.Color;

import com.mingzebj.bmpcn.qr.utils.LogCrashHandler;

/**
 * Created by liuchao on 2015/11/5.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        LogCrashHandler.getInstance().init(this);
    }
}
