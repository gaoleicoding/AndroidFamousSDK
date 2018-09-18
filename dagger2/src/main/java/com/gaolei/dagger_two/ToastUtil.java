package com.gaolei.dagger_two;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private Context context;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}