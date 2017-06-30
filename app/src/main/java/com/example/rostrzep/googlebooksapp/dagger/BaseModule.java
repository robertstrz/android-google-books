package com.example.rostrzep.googlebooksapp.dagger;

import com.appunite.rx.android.MyAndroidSchedulers;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public final class BaseModule {

    @Provides
    @UiScheduler
    Scheduler provideUiScheduler() {
        return MyAndroidSchedulers.mainThread();
    }

    @Provides
    @NetworkScheduler
    Scheduler provideNetworkScheduler() {
        return Schedulers.io();
    }
}
