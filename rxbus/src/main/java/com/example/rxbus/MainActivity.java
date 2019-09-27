package com.example.rxbus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class MainActivity extends RxAppCompatActivity {

    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(R.id.content);
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        RxBus.withActivity(this)
                .setEventCode(RxCodeConstants.JUMP_TYPE_TO_ONE)
                .onNext(rxEvent -> content.setText((String) rxEvent.getObject())
                ).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
