package com.andressantibanez.sarapp;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import net.danlew.android.joda.JodaTimeAndroid;
import net.grandcentrix.tray.TrayAppPreferences;
import net.grandcentrix.tray.accessor.ItemNotFoundException;

/**
 * Created by asantibanez on 2/20/16.
 */
public class Sarapp extends Application {

    private static Sarapp instance;

    private TrayAppPreferences mAppPreferences;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        JodaTimeAndroid.init(this);
        mAppPreferences = new TrayAppPreferences(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
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
