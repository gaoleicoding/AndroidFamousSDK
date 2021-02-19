package com.gl.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gl.rxjava.util.Rxjava2Util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    TextView tv_return;
    private CompositeDisposable mCompositeDisposable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_return = findViewById(R.id.tv_return);
        mCompositeDisposable = new CompositeDisposable();
        Rxjava2Util.rxjavaDemo();
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvancePolling();
            }
        });
    }

    /*
     *固定时延：使用intervalRange操作符，每间隔3s执行一次任务。
     */
    private void startSimplePolling() {
        Log.d(TAG, "startSimplePolling");

        Observable<Long> observable = Observable.intervalRange(0, 5, 0, 3000, TimeUnit.MILLISECONDS).take(5).doOnNext(new Consumer<Long>() {

            @Override
            public void accept(Long aLong) throws Exception {
                doWork(); //这里使用了doOnNext，因此DisposableObserver的onNext要等到该方法执行完才会回调。
            }

        });
        DisposableObserver<Long> disposableObserver = getDisposableObserver();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    /*
     *变长时延：使用repeatWhen操作符实现，第一次执行完任务后，等待4s再执行第二次任务，在第二次任务执行完成后，等待5s，依次递增。
     */
    private void startAdvancePolling() {

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
        mCompositeDisposable.add(disposableObserver);
    }

    private DisposableObserver<Long> getDisposableObserver() {

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

    private void doWork() {
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