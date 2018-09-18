package com.gaolei.dagger_two;

import dagger.Subcomponent;

@MainActivityScope
@Subcomponent
public interface MainFragmentComponent {

    void inject(MainFragment mainFragment);
}