package com.app.easymajdoor;

import android.app.Application;

import timber.log.Timber;

public class EasyMajdoorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
