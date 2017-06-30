package com.example.rostrzep.googlebooksapp.dagger;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.rostrzep.googlebooksapp.BaseActivity;

public interface BaseActivityComponent {

    BaseActivity baseActivity();
    @ForActivity
    Context context();
    LayoutInflater layoutInflater();
}
