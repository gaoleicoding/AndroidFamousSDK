package com.androidfamousframe.abc;

import android.app.Application;

import com.androidfamousframe.BuildConfig;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

import org.greenrobot.eventbus.EventBus;

public class DemoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new AppContext()).start();
        EventBus eventBus = EventBus.builder().addIndex(new MyEventBusIndex()).build();

    }


    //参数设置
    public class AppContext extends BlockCanaryContext {
        private static final String TAG = "AppContext";

        @Override
        public String provideQualifier() {
            String qualifier = "";
            qualifier += BuildConfig.VERSION_CODE + "_" + BuildConfig.VERSION_NAME + "_YYB";

            return qualifier;
        }

        @Override
        public int provideBlockThreshold() {
            return 500;
        }

        @Override
        public boolean displayNotification() {
            return BuildConfig.DEBUG;
        }

        @Override
        public boolean stopWhenDebugging() {
            return false;
        }
    }
}