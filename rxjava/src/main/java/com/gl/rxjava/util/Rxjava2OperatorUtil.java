package com.gl.rxjava.util;

import android.text.TextUtils;

import com.gl.rxjava.entity.Student;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class Rxjava2OperatorUtil {
    /**
     * subscribeOn
     * <p>
     * 线程切换
     */
    public static void subscribeOn() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 just() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 subscribe() 的回调发生在主线程
                .subscribe(var -> LogUtil.print("number:" + var));
    }

    /**
     * map
     * <p>
     * 同步的数据变换
     */
    public static void simpleMap() {
        Observable.just("Hello, world!")
                .map(s -> s + " -Dan")
                .subscribe(s -> LogUtil.print(s));
    }

    public static void rxjava2Map() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        })
                .subscribe(getObserver());
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        LogUtil.print(s);
//                    }
//                });

    }

    public static Observer getObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(String aLong) {
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

    /**
     * flatMap
     * <p>
     * 异步的数据变换
     */
    public static void flatMap() {
        Observable.just("Hello, world!")
                .flatMap(s -> Observable.just(s + " -Dan"))
                .subscribe(s -> LogUtil.print(s));
    }

    public static void simplerxjava2FlatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.print(s);
            }
        });
    }

    //    rxjava 2.x    flatmap解决网络请求嵌套问题
    public static void rxjava2FlatMap() {

        //构建数据
        final List<Student> list = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            Student stu = new Student();
            stu.id = i;
            stu.name = "学生" + i;
            List<Student.Course> courses = new ArrayList<>();
            for (int j = 0; j < 4; ++j) {
                Student.Course course = new Student.Course();
                course.setName(stu.name + "的课程" + j);
                course.setId(j);
                courses.add(course);
            }

            stu.setCoursesList(courses);
            list.add(stu);
        }

        /*创建observable将所有学生发送*/

        Observable.fromIterable(list)
                /*筛选学生：这里是学生id为0或2*/
                .filter(new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) throws Exception {
                        return student.id == 0 || student.id == 2;
                    }
                })
                /*将学生的课程发送出去，从学生实例得到课程实例，再发射出去*/
                .flatMap(new Function<Student, ObservableSource<Student.Course>>() {
                    @Override
                    public ObservableSource<Student.Course> apply(Student student) throws Exception {
                        LogUtil.print("flatmap student name = " + student.name);
                        return Observable.fromIterable(student.getCoursesList()).delay(10, TimeUnit.MILLISECONDS);
                    }
                })
                /*得到课程再筛选id为1或3的课程*/
                .filter(new Predicate<Student.Course>() {
                    @Override
                    public boolean test(Student.Course course) throws Exception {
                        return course.getId() == 1 || course.id == 3;
                    }
                })
                /*接受到学生的课程*/
                .subscribe(new Consumer<Student.Course>() {
                    @Override
                    public void accept(Student.Course course) throws Exception {
                        LogUtil.print("Consumer accept course = " + course.getName());
                    }
                });

    }


    public static void flowable() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (i < 200) {
                            if (e.requested() == 0) continue;//此处添加代码，让flowable按需发送数据
                            LogUtil.print("发射---->" + i);
                            i++;
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
//                .onBackpressureBuffer(1000)//设置缓存个数，默认128
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    private Subscription mSubscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);            //设置初始请求数据量为1
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(50);
                            LogUtil.print("接收------>" + integer);
                            mSubscription.request(1);//每接收到一条数据增加一条请求量
                        } catch (InterruptedException ignore) {
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * concat
     * <p>
     * 取数据时，先查找内存，再查找文件缓存，最后才查找网络
     */
    public static void concat() {
        String memory = "内存";
        String file = "文件";
        String net = "网络";
        Observable<String> memoryTask = Observable.create(subscriber -> {
            if (!TextUtils.isEmpty(memory)) {
                subscriber.onNext(memory);
            } else {
                subscriber.onComplete();
            }
        });
        Observable<String> fileTask = Observable.create(subscriber -> {
            if (!TextUtils.isEmpty(file)) {
                subscriber.onNext(file);
            } else {
                subscriber.onComplete();
            }
        });
        Observable<String> netTask = Observable.just(net);
        //特别提醒：这里的memoryTask、fileTask千万别用just创建，否则的话会直接返回memoryTask的值(哪怕memory为空)
        Observable.concat(memoryTask, fileTask, netTask)
                .first("")
                .subscribe(str -> LogUtil.print(str));
    }

    /**
     * zip
     * <p>
     * 等待多个请求完成
     */
    public static void zip() {
        LogUtil.print("开始请求");
        Observable<String> getA = Observable.create(subscriber -> {
            try {
                Thread.sleep(1000);
                LogUtil.print("A");
                subscriber.onNext("A");
                subscriber.onComplete();
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
        });
        Observable<String> getB = Observable.create(subscriber -> {
            try {
                Thread.sleep(2000);
                LogUtil.print("B");
                subscriber.onNext("B");
                subscriber.onComplete();
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
        });
        Observable.zip(getA, getB, (a, b) -> zipAB(a, b))
                .subscribe(var -> LogUtil.print(var));
    }

    private static String zipAB(String a, String b) {
        return a + "和" + b;
    }

    /**
     * combineLatest
     * <p>
     * 合并多个输入框的最新数据
     */
    public static void combineLatest() {
        LogUtil.print("开始请求");
        Observable<String> userNameEt = Observable.just("YTR");
        Observable<String> passwordEt = Observable.just("123456", "");
        Observable.combineLatest(userNameEt, passwordEt, (username, password) -> validate(username, password))
                .subscribe(var -> LogUtil.print("gaolei，" + var));
    }

    public static Boolean validate(String username, String password) {
        LogUtil.print("username=" + username + "\tpassword=" + password);
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    /**
     * debounce
     * <p>
     * 获取输入框的最新数据
     */
    public static void debounce() {
        LogUtil.print("开始");
        Observable<Integer> mockEt = Observable.create(subscriber -> {
            try {
                subscriber.onNext(1);
                Thread.sleep(500);
                subscriber.onNext(2);
                Thread.sleep(1500);
                subscriber.onNext(3);
                subscriber.onComplete();
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
        });
        mockEt.debounce(1, TimeUnit.SECONDS)//取1秒之内的最后一次
                .subscribe(var -> LogUtil.print(var.toString()));//输出2、3
    }

    /**
     * throttleFirst
     * <p>
     * 防止连续点击
     */
    public static void throttleFirst() {
        LogUtil.print("开始");
        Observable<Integer> mockBtn = Observable.create(subscriber -> {
            try {
                subscriber.onNext(1);
                Thread.sleep(500);
                subscriber.onNext(2);
                Thread.sleep(1500);
                subscriber.onNext(3);
                subscriber.onComplete();
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
        });
        mockBtn.throttleFirst(1, TimeUnit.SECONDS)//取1秒之内的第一次
                .subscribe(var -> LogUtil.print(var.toString()));//输出1、3
    }

    /**
     * throttleLast
     * <p>
     * 防止连续点击
     */
    public static void throttleLast() {
        LogUtil.print("开始");
        Observable<Integer> mockBtn = Observable.create(subscriber -> {
            try {
                subscriber.onNext(1);
                Thread.sleep(500);
                subscriber.onNext(2);
                Thread.sleep(1500);
                subscriber.onNext(3);
                subscriber.onComplete();
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
        });
        mockBtn.throttleLast(3, TimeUnit.SECONDS)//取3秒之内的最后一次
                .subscribe(var -> LogUtil.print(var.toString()));//输出3
    }

    /**
     * interval
     * <p>
     * 定时操作
     */
    public static void interval() {
        LogUtil.print("开始");
        //延迟2秒后，每隔3秒发送一次
        Observable.interval(2, 3, TimeUnit.SECONDS)
                .subscribe(var -> LogUtil.print(var.toString()));
    }

    /**
     * filter、distinct、take、reduce
     * <p>
     * 复杂的数据变换
     */
    public static void filter() {
        Observable.just("1", "2", "2", "3", "4", "5")
                .map(Integer::parseInt)//转换为int
                .filter(s -> s > 1)//取大于1
                .distinct()//去重
                .take(2)//取前两位
                .reduce((integer, integer2) -> integer.intValue() + integer2.intValue())//迭代计算
                .subscribe(var -> LogUtil.print(var.toString()));//输出：5
    }

    /**
     * defer
     * <p>
     * 包装缓慢的旧代码
     */
    public static void defer() {
        //这样的话，还是会阻塞主线程
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = Observable.just(blockMethod("A"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var -> LogUtil.print(var.toString()));
        compositeDisposable.add(disposable);
        compositeDisposable.dispose();
        compositeDisposable.clear();
        //使用defer的话，就不会阻塞主线程
        Observable.defer(() -> Observable.just(blockMethod("B")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var -> LogUtil.print(var.toString()));
    }

    public static String blockMethod(String msg) {
        String result = "block:" + msg;
        LogUtil.print(result);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
