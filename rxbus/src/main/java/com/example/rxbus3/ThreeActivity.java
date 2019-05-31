package com.example.rxbus3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.rxbus.R;

public class ThreeActivity extends BaseActivity {

    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        content = findViewById(R.id.content);

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThreeActivity.this, FourActivity.class));
            }
        });

        RxBus3.withActivity(this)
                .setEventCode(RxBusConstants.EVENT_ACTION_CAR_LOCATION)
                .onNext(rxEvent ->
                        content.setText((String)rxEvent.getContent())
                ).create();

    }
}
