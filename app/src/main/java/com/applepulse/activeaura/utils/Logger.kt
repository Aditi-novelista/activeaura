package com.applepulse.activeaura.utils

import android.util.Log
import com.applepulse.activeaura.BuildConfig


object Logger {

    fun errorLog(value: String) {
        Log.e("", value)
    }

    fun debugLog(value: String) {
        Log.d("", value)
    }

    fun debugLog(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg!!)
        }
    }
    
}