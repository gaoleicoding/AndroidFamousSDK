package com.gaolei.dagger_one;

import dagger.Component;

@Component(modules = SampleModule.class)
public interface SampleComponent {

    void inject(MainActivity activity);

}