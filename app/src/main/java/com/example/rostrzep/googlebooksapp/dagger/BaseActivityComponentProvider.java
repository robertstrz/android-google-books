package com.example.rostrzep.googlebooksapp.dagger;

import android.os.Bundle;

public interface BaseActivityComponentProvider {

    void inject(Bundle savedInstanceState, AppComponent appComponent);

}
