package com.gl.frame;

import android.app.Application;

import com.gl.frame.umneg.MyPreferences;
import com.gl.frame.umneg.helper.PushHelper;
import com.umeng.commonsdk.utils.UMUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PushHelper.preInit(this);
        initPushSDK();
    }
    /**
     * 场景一：用户隐私政策协议同意后，再初始化（用户同意后，别忘记调用初始化～）
     */
    private void initPushSDK() {
        boolean agreed =  MyPreferences.getInstance(getApplicationContext()).hasAgreePrivacyAgreement();
        if (agreed) {
            PushHelper.init(getApplicationContext());
        }
    }
    /**
     * 场景二：考虑冷启动速度等，在子线程做初始化
     */
    private void initPushSDK2() {
        if (UMUtils.isMainProgress(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PushHelper.init(getApplicationContext());
                }
            }).start();
        }
    }
}