package com.example.rxbus;


import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.Arrays;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * RxBus
 *
 * @author misha
 * @date 2017/5/18
 */

public class RxBus {

    private static RxBus rxBus;
    /**
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     */
    private final Subject<RxEvent> _bus = PublishSubject.<RxEvent>create().toSerialized();

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(RxEvent o) {
        _bus.onNext(o);
    }

    public void send(int code, Object content) {
        RxEvent event = new RxEvent();
        event.setEventCode(code);
        event.setContent(content);
        send(event);
    }

    private Observable<RxEvent> toObservable() {
        return _bus;
    }

    public static SubscriberBuilder withActivity(LifecycleProvider<ActivityEvent> provider) {
        return new SubscriberBuilder().setActivityLifeCycleProvider(provider);
    }

    public static SubscriberBuilder withFragment(LifecycleProvider<FragmentEvent> provider) {
        return new SubscriberBuilder().setFragmentLifeCycleProvider(provider);
    }

    public static SubscriberBuilder withNoContext() {
        return new SubscriberBuilder();
    }

    public static class SubscriberBuilder {

        private LifecycleProvider<ActivityEvent> mActivityLifecycleProvider;
        private LifecycleProvider<FragmentEvent> mFragmentLifecycleProvider;
        private FragmentEvent mFragmentEndEvent;
        private ActivityEvent mActivityEndEvent;
        private HashSet<Integer> mEventCodeSet = new HashSet<>();
        private Consumer<? super RxEvent> onNext;
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

        public SubscriberBuilder onNext(Consumer<? super RxEvent> action) {
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Consumer<Throwable> action) {
            this.onError = action;
            return this;
        }

        public Disposable create() {
            if (mFragmentLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(mFragmentEndEvent != null ? RxLifecycle.<RxEvent, FragmentEvent>bindUntilEvent(mFragmentLifecycleProvider.lifecycle(), mFragmentEndEvent)
                                : RxLifecycleAndroid.<RxEvent>bindFragment(mFragmentLifecycleProvider.lifecycle()))
                        .filter(new Predicate<RxEvent>() {
                            @Override
                            public boolean test(RxEvent rxEvent) throws Exception {
                                return mEventCodeSet.contains(rxEvent.getEventCode());
                            }
                        })   //过滤 根据code判断返回事件
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        } : onError);
            } else if (mActivityLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(mActivityEndEvent != null ? RxLifecycle.<RxEvent, ActivityEvent>bindUntilEvent(mActivityLifecycleProvider.lifecycle(), mActivityEndEvent)
                                : RxLifecycleAndroid.<RxEvent>bindActivity(mActivityLifecycleProvider.lifecycle()))
                        .filter(new Predicate<RxEvent>() {
                            @Override
                            public boolean test(RxEvent rxEvent) throws Exception {
                                return mEventCodeSet.contains(rxEvent.getEventCode());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        } : onError);
            } else {
                return RxBus.getInstance().toObservable()
                        .filter(new Predicate<RxEvent>() {
                            @Override
                            public boolean test(RxEvent rxEvent) throws Exception {
                                return mEventCodeSet.contains(rxEvent.getEventCode());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        } : onError);
            }
        }
    }
}
