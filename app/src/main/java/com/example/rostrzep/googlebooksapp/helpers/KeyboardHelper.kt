package com.example.rostrzep.googlebooksapp.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardHelper {

    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.window.decorView
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
