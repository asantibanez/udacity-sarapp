package com.andressantibanez.sarapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by asantibanez on 2/20/16.
 */
public class Sarapp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context context() {
        return mContext;
    }
}
