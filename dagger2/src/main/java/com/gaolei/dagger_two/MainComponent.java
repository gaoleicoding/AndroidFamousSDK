package com.gaolei.dagger_two;


import dagger.Component;

@MainActivityScope
@Component(dependencies = {ActivityComponent.class}, modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);

    MainFragmentComponent mainFragmentComponent();
}