package com.gaolei.dagger_two;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    public Activity provideActivity() {
        return this.activity;
    }
}