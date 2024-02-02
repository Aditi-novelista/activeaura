package com.applepulse.activeaura.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


open class BaseViewModel(application: Application) :
    AndroidViewModel(application) {
    /**
     * LiveData to show progress in activity/fragment
     */
    val progressLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
}