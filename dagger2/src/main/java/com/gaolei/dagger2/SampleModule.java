package com.gaolei.dagger2;

import dagger.Module;

@Module
public class SampleModule {

    private SampleActivity activity;

    public SampleModule(SampleActivity activity) {
        this.activity = activity;
    }
}