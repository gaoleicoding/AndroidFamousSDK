package com.gaolei.dagger_one;

import dagger.Module;
import dagger.Provides;

@Module
public class SampleModule {
    // 关键字，标明该方法提供依赖对象
    @Provides
    Student providerPerson() {
        //提供Person对象
        return new Student();
    }
}