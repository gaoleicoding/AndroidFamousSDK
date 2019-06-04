package com.example.rxbus4;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.app.AppCompatActivity;

import com.example.rxbus.RxBusBaseMessage;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.Arrays;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class RxBus4 {

    private static volatile RxBus4 instance;

    private Subject<RxBusBaseMessage> bus;

    public static RxBus4 getInstance() {
        if (instance == null) {
            synchronized (RxBus4.class) {
                if (instance == null) {
                    instance = new RxBus4();
                }
            }
        }
        return instance;
    }

    public RxBus4() {
        bus = PublishSubject.<RxBusBaseMessage>create().toSerialized();
    }


    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param code
     * @param o
     */
    public void send(int code, Object o) {
        bus.onNext(new RxBusBaseMessage(code, o));
    }


    /**
     * 根据传递的eventtype类型返回特定类型（eventype）的被观察者
     */
    public Observable<RxBusBaseMessage> tObservable(Class<RxBusBaseMessage> EventType) {
        return bus.ofType(EventType);
    }

    /***
     *  * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     */
    public Observable<RxBusBaseMessage> toObservable() {
        return bus;

    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasSubscribers() {
        return bus.hasObservers();
    }

    /**
     * 注销
     */
    public void unRegisterAll() {
        bus.onComplete();
    }

  
    private Consumer<? super RxBusBaseMessage> onNext;
    public static class SubscriberBuilder {

        private LifecycleProvider<ActivityEvent> mActivityLifecycleProvider;
        private LifecycleProvider<FragmentEvent> mFragmentLifecycleProvider;
        private FragmentEvent mFragmentEndEvent;
        private ActivityEvent mActivityEndEvent;
        private HashSet<Integer> mEventCodeSet = new HashSet<>();
        private Consumer<? super RxBusBaseMessage> onNext;
        private Consumer<Throwable> onError;

        public SubscriberBuilder setActivityLifeCycleProvider(LifecycleProvider<ActivityEvent> provider) {
            this.mActivityLifecycleProvider = provider;
            return this;
        }

        public SubscriberBuilder setFragmentLifeCycleProvider(LifecycleProvider<FragmentEvent> provider) {
            this.mFragmentLifecycleProvider = provider;
            return this;
        }

        public SubscriberBuilder setEventCode(Integer eventCode) {
            this.mEventCodeSet.add(eventCode);
            return this;
        }

        public SubscriberBuilder setEventCodeArray(Integer[] eventCodeArray) {
            this.mEventCodeSet.addAll(Arrays.asList(eventCodeArray));
            return this;
        }

        public SubscriberBuilder setFragmentEndEvent(FragmentEvent endEvent) {
            this.mFragmentEndEvent = endEvent;
            return this;
        }

        public SubscriberBuilder setActivityEndEvent(ActivityEvent endEvent) {
            this.mActivityEndEvent = endEvent;
            return this;
        }

        public SubscriberBuilder onNext(Consumer<? super RxBusBaseMessage> action) {
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Consumer<Throwable> action) {
            this.onError = action;
            return this;
        }

        public Disposable create(LifecycleOwner lifecycleOwner) {
            return RxBus4.getInstance().toObservable()
                    .filter(new Predicate<RxBusBaseMessage>() {
                        @Override
                        public boolean test(RxBusBaseMessage rxEvent) throws Exception {
                            return mEventCodeSet.contains(rxEvent.getCode());
                        }
                    })   //过滤 根据code判断返回事件
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(AutoDispose.<RxBusBaseMessage>autoDisposable(
                            AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                    .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    } : onError)
                    ;
                    
        }
    }
    public static SubscriberBuilder withActivity(LifecycleProvider<ActivityEvent> provider) {
        return new SubscriberBuilder().setActivityLifeCycleProvider(provider);
    }

}