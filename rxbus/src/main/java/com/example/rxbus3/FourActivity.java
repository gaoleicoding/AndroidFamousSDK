package com.example.rxbus3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.rxbus.R;

public class FourActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus3.getInstance().send(RxBusConstants.EVENT_ACTION_CAR_LOCATION, "From FourActivity");
            }
        });


    }
}
