package com.example.rostrzep.googlebooksapp.dagger;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;

import com.example.rostrzep.googlebooksapp.BaseActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    BaseActivity activity() {
        return activity;
    }

    @Provides
    @ForActivity
    Context context() {
        return activity;
    }

    @Provides
    @ForActivity
    Resources resources() {
        return activity.getResources();
    }

    @Provides
    static LayoutInflater provideLayoutInflater(BaseActivity activity) {
        return activity.getLayoutInflater();
    }
}
