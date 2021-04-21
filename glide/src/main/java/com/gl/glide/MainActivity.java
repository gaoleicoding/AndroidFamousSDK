package com.gl.glide;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = findViewById(R.id.image);
        String url = "https://t7.baidu.com/it/u=1856946436,1599379154&fm=193&f=GIF";
        ImageLoader.getInstance().load(this, url, image);
    }
}