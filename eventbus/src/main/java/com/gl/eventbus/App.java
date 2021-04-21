package com.gl.eventbus;

import android.app.Application;

import com.androidfamousframe.abc.MyEventBusIndex;

import org.greenrobot.eventbus.EventBus;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //MyEventBusIndex的文件必须在注册、订阅和发送完成的时候才会生成
        EventBus eventBus = EventBus.builder().addIndex(new MyEventBusIndex()).build();

    }

}