package com.example.rxbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxBus.withActivity()
                .setEventCode(RxBusConstant.EVENTCODE_ODER_DETAIL_REFRESH)
                .onNext(rxEvent ->
                        if(rxE)
                        ).create();
    }
}
