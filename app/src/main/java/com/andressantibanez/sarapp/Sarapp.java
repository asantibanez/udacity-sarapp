package com.andressantibanez.sarapp;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;
import net.grandcentrix.tray.TrayAppPreferences;
import net.grandcentrix.tray.accessor.ItemNotFoundException;

/**
 * Created by asantibanez on 2/20/16.
 */
public class Sarapp extends Application {

    private static Sarapp instance;

    private TrayAppPreferences mAppPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        JodaTimeAndroid.init(this);
        mAppPreferences = new TrayAppPreferences(this);
    }

    public static Sarapp instance() {
        return instance;
    }

    public void setToken(String token) {
        mAppPreferences.put("token", token);
    }

    public String getToken() {
        try {
            return mAppPreferences.getString("token");
        } catch (ItemNotFoundException e) {
            //Should never happen
            return "";
        }
    }

}
