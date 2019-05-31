package com.example.rxbus3;

import com.google.gson.JsonParseException;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RequestUtil {
    public static <T> ObservableTransformer<T, T> threadTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread());
    }

    public static <T extends Data> Observable<T> request(Observable<ICarsClubResponse<T>> observable) {
        Observable<T> upstream = observable
                .compose(threadTransformer())
                .map(new CommonFilter<>());
        return upstream;
    }

    public static class CommonFilter<T extends Data>
            implements Function<ICarsClubResponse<T>, T> {
        @Override
        public T apply(ICarsClubResponse<T> t) throws Exception {
            int code = t.getStatus().getCode();
            // 6强制更新 7选择更新 0请求成功
            if (code == 6) {
                throw new HttpRuntimeException(-1, "必需更新应用后才能正常使用");
            }
            if (code == 0 || code == 7) {
                return t.getData();
            }
            throw new HttpRuntimeException(code, t.getStatus().getMessage());
        }
    }

    public static abstract class CommonObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T t) {
            success(t);
        }

        @Override
        public void onError(Throwable throwable) {
            if (throwable instanceof HttpRuntimeException) {
                error((HttpRuntimeException) throwable);
            } else if (throwable instanceof JsonParseException) {
                error(new HttpRuntimeException(ResponseCode.ERROR_JSON, "json error"));
            } else if (throwable instanceof HttpException) {
                HttpException he = (HttpException) throwable;
                int code = he.code();
                error(new HttpRuntimeException(code, "Http 错误: " + code));
            } else if (throwable instanceof IOException) {
                IOException ioe = (IOException) throwable;
                error(new HttpRuntimeException(ResponseCode.ERROR_NETWORK,
                        "网络错误: " + ioe.getClass().getSimpleName() + " " + ioe.getMessage()));
            } else {
                error(new HttpRuntimeException(ResponseCode.ERROR_NETWORK,
                        throwable.getClass().getSimpleName() + " " + throwable.getMessage()));
            }
        }

        @Override
        public void onComplete() {

        }

        public abstract void success(T t);

        public abstract void error(HttpRuntimeException e);
    }

    public static class HttpRuntimeException extends RuntimeException {
        private int code;

        HttpRuntimeException(int code, String message) {
            super(message);
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
