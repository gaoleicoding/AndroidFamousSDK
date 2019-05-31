package com.example.rxbus3;

import com.google.gson.JsonParseException;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public class RXLifeCycleUtil {

    public interface RXLifeCycleInterface {
        <T> LifecycleTransformer<T> bindUntil();

        <T> LifecycleTransformer<T> bindLifeCycle();
    }

    public interface RequestCallback3<T extends Data> {
        void onSuccess(T data);

        void onFail(int code, String message);
    }

    public static <T extends Data> Disposable request(Observable<ICarsClubResponse<T>> observable, RXLifeCycleInterface rxLifeCycleInterface, RequestCallback3<T> callback) {
        Disposable disposable = observable
                .compose(RequestUtil.threadTransformer())
                .compose(rxLifeCycleInterface.bindUntil())
                .subscribe(bs -> {

                }, throwable -> {
                    if (throwable instanceof JsonParseException) {
                        callback.onFail(ResponseCode.ERROR_JSON, "json error");
                    } else if (throwable instanceof HttpException) {
                        HttpException he = (HttpException) throwable;
                        int code = he.code();
                        callback.onFail(code, "Http 错误: " + code);
                    } else if (throwable instanceof IOException) {
                        IOException ioe = (IOException) throwable;
                        callback.onFail(ResponseCode.ERROR_NETWORK, "网络错误: " + ioe.getClass().getSimpleName() + " " + ioe.getMessage());
                    } else {
                        callback.onFail(ResponseCode.ERROR_UNKNOWN, throwable.getClass().getSimpleName() + " " + throwable.getMessage());
                    }
                });
        return disposable;
    }

}
