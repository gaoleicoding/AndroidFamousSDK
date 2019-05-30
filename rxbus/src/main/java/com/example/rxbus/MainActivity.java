package com.example.rxbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content=findViewById(R.id.content);

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RxBus.getInstance().tObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage.class)
                .subscribe(new Consumer<RxBusBaseMessage>() {
                    @Override
                    public void accept(RxBusBaseMessage baseMessage) throws Exception {
                        content.setText(baseMessage.getObject().);
                    }
                });

    }
}
