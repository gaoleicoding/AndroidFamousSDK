package com.example.rxbus3;

import android.os.Bundle;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public class BaseActivity extends RxAppCompatActivity implements RXLifeCycleUtil.RXLifeCycleInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public <T> LifecycleTransformer<T> bindUntil() {
        return this.bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public <T> LifecycleTransformer<T> bindLifeCycle() {
        return this.bindToLifecycle();
    }


}
