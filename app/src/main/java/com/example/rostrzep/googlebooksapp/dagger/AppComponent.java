package com.example.rostrzep.googlebooksapp.dagger;

import android.content.Context;

import com.example.rostrzep.googlebooksapp.MainApplication;
import com.example.rostrzep.googlebooksapp.api.GoogleBooksService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import rx.Scheduler;

@Singleton
@Component(
        modules = {
                AppModule.class,
                BaseModule.class
        }

)
public interface AppComponent {

    void inject(MainApplication app);

    @UiScheduler
    Scheduler getUiScheduler();

    @NetworkScheduler
    Scheduler getNetworkScheduler();

    @ForApplication
    Context getContext();


    OkHttpClient getOkHttpClient();

    Gson getGson();

    Picasso picasso();

    GoogleBooksService googleBooksService();
}