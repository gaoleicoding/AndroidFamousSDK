package com.gl.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gl.rxjava.util.Rxjava2Util;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    TextView tv_return;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_return = findViewById(R.id.tv_return);
        Rxjava2Util.rxjavaDemo();
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Rxjava2Util.sheduleThread();
            }
        });
    }

}