package com.gl.glide;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static  Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

    }

}