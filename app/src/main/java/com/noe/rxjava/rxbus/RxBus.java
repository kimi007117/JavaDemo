package com.noe.rxjava.rxbus;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by lijie on 2020-05-06.
 */
public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private SerializedSubject<Event, Event> rxBus;
    private SerializedSubject<Event, Event> rxStickBus;

    private RxBus() {
        this.rxBus = new SerializedSubject(PublishSubject.create());
        this.rxStickBus = new SerializedSubject(BehaviorSubject.create());
    }

    public static RxBus getInstance() {
        return RxBus.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static RxBus instance = new RxBus();

        private SingletonHolder() {
        }
    }

    public void postEvent(Event event) {
        Log.d("RxEvent", "postEvent action : " + event.action());
        if (this.hasObservers()) {
            this.rxBus.onNext(event);
        }

    }

    public void postEmptyEvent(String action) {
        Log.d("RxEvent", "postEmptyEvent action : " + action);
        if (this.hasObservers()) {
            this.rxBus.onNext(new EmptyEvent(action));
        }

    }

    public void postStickEvent(Event event) {
        this.rxStickBus.onNext(event);
    }

    public void postStickEmptyEvent(String action) {
        this.rxStickBus.onNext(new EmptyEvent(action));
    }

    public Observable<Event> toObservable(final String action) {
        return this.rxBus.asObservable().filter(new Func1<Event, Boolean>() {
            public Boolean call(Event event) {
                return event.action().equals(action);
            }
        }).onBackpressureBuffer();
    }

    public Observable<Event> toObservable(final List<String> actions) {
        if (actions == null) {
            throw new NullPointerException("[RxBus.toObservable] actions cannot be null");
        } else {
            return this.rxBus.asObservable().filter(new Func1<Event, Boolean>() {
                public Boolean call(Event event) {
                    return actions.contains(event.action());
                }
            }).onBackpressureBuffer();
        }
    }

    public Observable<Event> toObservableOnMain(String action) {
        return this.toObservable(action).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> toObservableOnMain(String... actions) {
        return this.toObservableOnMain(Arrays.asList(actions));
    }

    public Observable<Event> toObservableOnMain(List<String> actions) {
        return this.toObservable(actions).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> toStickObservable(final String action) {
        return this.rxStickBus.asObservable().filter(new Func1<Event, Boolean>() {
            public Boolean call(Event event) {
                return event.action().equals(action);
            }
        }).onBackpressureBuffer();
    }

    public Observable<Event> toStickObservable(final List<String> actions) {
        if (actions == null) {
            throw new NullPointerException("[RxBus.toStickObservable] actions cannot be null");
        } else {
            return this.rxStickBus.asObservable().filter(new Func1<Event, Boolean>() {
                public Boolean call(Event event) {
                    return actions.contains(event.action());
                }
            }).onBackpressureBuffer();
        }
    }

    public Observable<Event> toStickObservableOnMain(String action) {
        return this.toStickObservable(action).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> toStickObservableOnMain(List<String> actions) {
        return this.toStickObservable(actions).observeOn(AndroidSchedulers.mainThread());
    }

    private boolean hasObservers() {
        return this.rxBus.hasObservers();
    }

}
