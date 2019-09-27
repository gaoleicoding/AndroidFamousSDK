package com.androidfamousframe.abc;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidfamousframe.R;
import com.androidfamousframe.abc.eventbus.MessageEvent;
import com.androidfamousframe.abc.glide.MyGlideUrl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new MessageEvent());
        Glide.with(this)
                    .load(new MyGlideUrl("www.baidu.com"))
                    .dontAnimate() //去掉显示动画
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //DiskCacheStrategy.NONE
                    .into(new ImageView(this));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {/* Do something */};
}
