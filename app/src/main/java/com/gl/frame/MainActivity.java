package com.gl.frame;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gl.frame.umneg.MyPreferences;
import com.gl.frame.umneg.helper.PushHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //用户点击隐私协议同意按钮后，初始化PushSDK
        findViewById(R.id.btn_privacy_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPreferences.getInstance(getApplicationContext()).setAgreePrivacyAgreement(true);
                PushHelper.init(getApplicationContext());
            }
        });
    }
}
