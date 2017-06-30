package com.example.rostrzep.googlebooksapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rostrzep.googlebooksapp.dagger.BaseActivityComponentProvider;


public abstract class BaseActivity extends AppCompatActivity implements BaseActivityComponentProvider {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inject(savedInstanceState, ((MainApplication) getApplication()).getAppComponent());
        super.onCreate(savedInstanceState);
    }

}