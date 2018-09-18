package com.gaolei.dagger_two;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    public UserRepository provideUserRepository() {
        return new UserRepository();
    }
}