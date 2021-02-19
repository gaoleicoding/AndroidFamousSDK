package com.gl.rxjava.util;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Rxjava2Util {

    public static final String TAG = "gaolei";

    public static void sheduleThread() {

        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "数据源:" + Thread.currentThread().getName());
                emitter.onNext("数据源");
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                                                     @Override
                                                     public void subscribe(ObservableEmitter<String> e) throws Exception {
                                                         e.onNext(s + "------>" + "数据源切换");
                                                         Log.d(TAG, s + "------>" + "数据源切换:" + Thread.currentThread().getName());
                                                     }
                                                 }
                        ).subscribeOn(AndroidSchedulers.mainThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.d(TAG, s + "--->" + "第1次变化:" + ":" + Thread.currentThread().getName());
                        return s + "---> " + "第1次变化";
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.d(TAG, s + "---> " + "第2次变化:" + Thread.currentThread().getName());
                        return s + "---> " + "第2次变化";
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.d(TAG, s + " --->" + "第3次变化:" + Thread.currentThread().getName());
                        return s + " --->" + "第3次变化";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.d(TAG, s + " --->" + "第4次变化:" + Thread.currentThread().getName());
                        return s + " --->";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "监听者" + Thread.currentThread().getName());
                    }
                });
    }

    public static void rxjavaDemo() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "数据源:" + Thread.currentThread().getName());
                emitter.onNext("数据源");
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(getObserver());
    }


    public static Observer getObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(String aLong) {
                Log.d(TAG, "我接收到数据了:" + Thread.currentThread().getName());
                System.out.println("我接收到数据了");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
        return observer;
    }
}
