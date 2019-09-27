package com.androidfamousframe.abc;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidfamousframe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

            Glide.with(this)
                    .load("")
                    .dontAnimate() //去掉显示动画
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //DiskCacheStrategy.NONE
                    .into(new ImageView(this));

    }
}
