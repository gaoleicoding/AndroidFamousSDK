package com.gaolei.dagger_two;

import android.os.Bundle;

import com.gaolei.dagger2.R;

public class MainActivity extends BaseActivity {

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainComponent = DaggerMainComponent.builder()
                .activityComponent(getActivityComponent())
                .mainModule(new MainModule())
                .build();
        mainComponent.inject(this);
    }

    public MainComponent getMainComponent() {
        return this.mainComponent;
    }
}