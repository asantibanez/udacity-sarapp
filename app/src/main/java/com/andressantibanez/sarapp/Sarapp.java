package com.andressantibanez.sarapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by asantibanez on 2/20/16.
 */
public class Sarapp extends Application {

    private static Sarapp instance;

    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Sarapp instance() {
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
