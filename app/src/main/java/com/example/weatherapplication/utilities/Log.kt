package com.example.weatherapplication.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager

fun LogD(tag: String, msg: String){
    Log.d(tag, msg)
}
fun LogE(tag: String, msg: String){
    Log.e(tag, msg)
}

fun closeKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
