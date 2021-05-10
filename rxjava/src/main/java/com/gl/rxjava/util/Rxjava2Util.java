package com.gl.rxjava.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
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


    /*
     *固定时延：使用intervalRange操作符，每间隔3s执行一次任务。
     */
    public static void startSimplePolling() {
        Log.d(TAG, "startSimplePolling");

        Observable<Long> observable = Observable.intervalRange(0, 5, 0, 3000, TimeUnit.MILLISECONDS).take(5).doOnNext(new Consumer<Long>() {

            @Override
            public void accept(Long aLong) throws Exception {
                doWork(); //这里使用了doOnNext，因此DisposableObserver的onNext要等到该方法执行完才会回调。
            }

        });
        DisposableObserver<Long> disposableObserver = getDisposableObserver();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }

    /*
     *变长时延：使用repeatWhen操作符实现，第一次执行完任务后，等待4s再执行第二次任务，在第二次任务执行完成后，等待5s，依次递增。
     */
    public static void startAdvancePolling() {

        Log.d(TAG, "startAdvancePolling click");
        Observable<Long> observable = Observable.just(0L).doOnComplete(new Action() {

            @Override
            public void run() throws Exception {
                doWork();
            }

        }).repeatWhen(new Function<Observable<Object>, ObservableSource<Long>>() {

            private long mRepeatCount;

            @Override
            public ObservableSource<Long> apply(Observable<Object> objectObservable) throws Exception {
                //必须作出反应，这里是通过flatMap操作符。
                return objectObservable.flatMap(new Function<Object, ObservableSource<Long>>() {

                    @Override
                    public ObservableSource<Long> apply(Object o) throws Exception {
                        if (++mRepeatCount > 4) {
                            //return Observable.empty(); //发送onComplete消息，无法触发下游的onComplete回调。
                            return Observable.error(new Throwable("Polling work finished")); //发送onError消息，可以触发下游的onError回调。
                        }
                        Log.d(TAG, "startAdvancePolling apply");
                        return Observable.timer(3000 + mRepeatCount * 1000, TimeUnit.MILLISECONDS);
                    }

                });
            }

        });
        DisposableObserver<Long> disposableObserver = getDisposableObserver();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }

    public static DisposableObserver<Long> getDisposableObserver() {

        return new DisposableObserver<Long>() {

            @Override
            public void onNext(Long aLong) {
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "DisposableObserver onError, threadId=" + Thread.currentThread().getId() + ",reason=" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "DisposableObserver onComplete, threadId=" + Thread.currentThread().getId());
            }
        };
    }

    private static void doWork() {
        long workTime = (long) (Math.random() * 500) + 500;
        try {
            Log.d(TAG, "doWork start,  threadId=" + Thread.currentThread().getId());
            Thread.sleep(workTime);
            Log.d(TAG, "doWork finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
