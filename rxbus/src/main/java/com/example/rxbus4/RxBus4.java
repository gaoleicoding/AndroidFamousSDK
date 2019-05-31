package com.example.rxbus4;


import com.example.rxbus.RxBusBaseMessage;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by hushendian on 2017/11/16.
 */

public class RxBus4 {

    private static volatile RxBus4 instance;

    private Subject<Object> bus;

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
        bus = PublishSubject.create().toSerialized();
    }

    public void send(Object o) {
        bus.onNext(o);
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

    public Observable<Object> toObservable() {
        return bus;
    }

    /**
     * 根据传递的eventtype类型返回特定类型（eventype）的被观察者
     */
    public <T> Observable<T> tObservable(Class<T> EventType) {
        return bus.ofType(EventType);
    }

    /***
     *  * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     */
    public <T> Observable<T> tObservable(final int code, final Class<T> eventType) {
        return bus.ofType(RxBusBaseMessage.class).filter(new Predicate<RxBusBaseMessage>() {
            @Override
            public boolean test(RxBusBaseMessage o) throws Exception {

                return o.getCode() == code && eventType.isInstance(o.getObject());

            }
        }).map(new Function<RxBusBaseMessage, Object>() {
            @Override
            public Object apply(RxBusBaseMessage o) throws Exception {
                return o.getObject();
            }
        }).cast(eventType);

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
}