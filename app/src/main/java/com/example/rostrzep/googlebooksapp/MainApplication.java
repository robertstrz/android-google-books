package com.example.rostrzep.googlebooksapp;

import android.app.Application;

import com.example.rostrzep.googlebooksapp.dagger.AppComponent;
import com.example.rostrzep.googlebooksapp.dagger.AppModule;
import com.example.rostrzep.googlebooksapp.dagger.DaggerAppComponent;

public class MainApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}