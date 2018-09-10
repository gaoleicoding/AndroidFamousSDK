package com.gaolei.dagger2;

import dagger.Component;

@Component(modules = SampleModule.class)
public interface SampleComponent {

    void inject(MainActivity activity);

}