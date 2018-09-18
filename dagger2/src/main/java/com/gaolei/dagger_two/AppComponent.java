package com.gaolei.dagger_two;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getContext();

    ToastUtil getToastUtil();
}