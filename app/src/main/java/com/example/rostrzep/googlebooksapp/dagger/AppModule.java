package com.example.rostrzep.googlebooksapp.dagger;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.rostrzep.googlebooksapp.AppConst;
import com.example.rostrzep.googlebooksapp.BuildConfig;
import com.example.rostrzep.googlebooksapp.MainApplication;
import com.example.rostrzep.googlebooksapp.api.GoogleBooksService;
import com.example.rostrzep.googlebooksapp.api.exceptions.UnknownServerException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class AppModule {

    private final MainApplication application;

    public AppModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @ForApplication
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    static Picasso providePicasso(@ForApplication Context context) {
        final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

        return new Picasso.Builder(context)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(BuildConfig.DEBUG)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        if (BuildConfig.DEBUG) {
                            Log.e("picasso", uri.toString(), exception);
                        }
                    }
                })
                .downloader(new OkHttp3Downloader(okHttpClient.build()))
                .build();
    }



    @Singleton
    @Provides
    static Gson provideGson() {
        return new GsonBuilder()
                .create();
    }


    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request().newBuilder()
                                .addHeader("Accept", "application/json")
                                .build());
                    }
                })
                .addInterceptor(new ErrorHandlingInterceptor())
                .addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit(final Gson gson,
                                    final OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(AppConst.getBaseUrl())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    static GoogleBooksService provideGoogleBooksService(Retrofit retrofit) {
        return retrofit.create(GoogleBooksService.class);
    }


    private static class ErrorHandlingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            return handleError(chain.proceed(chain.request()));
        }

        public Response handleError(Response response) throws IOException {
            if (response.isSuccessful()) {
                return response;
            }
            final int statusCode = response.code();
            if (statusCode < 200) {
                return response;
            }
            if (statusCode > HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new UnknownServerException();
            }
            return response;
        }
    }

}
