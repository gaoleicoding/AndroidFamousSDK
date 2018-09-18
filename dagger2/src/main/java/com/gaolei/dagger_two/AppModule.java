package com.gaolei.dagger_two;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return this.context;
    }


    @Singleton
    @Provides
    public ToastUtil provideToastUtil(Context context) {
        return new ToastUtil(context);
    }
}