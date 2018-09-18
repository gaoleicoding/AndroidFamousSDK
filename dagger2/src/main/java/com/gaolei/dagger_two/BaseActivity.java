package com.gaolei.dagger_two;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }
}