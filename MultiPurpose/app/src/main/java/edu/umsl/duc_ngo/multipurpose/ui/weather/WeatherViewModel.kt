package edu.umsl.duc_ngo.multipurpose.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    private var mIsFetch = MutableLiveData<Boolean>()
    private var mJsonResult = MutableLiveData<String>()

    init {
        mIsFetch.value = true
        mJsonResult.value = ""
    }

    fun getIsFetch(): LiveData<Boolean> {
        return mIsFetch
    }

    fun setIsFetch(bool: Boolean) {
        mIsFetch.value = bool
    }

    fun getJsonResult(): String {
        return mJsonResult.value!!
    }

    fun setJsonResult(body: String) {
        mJsonResult.value = body
    }
}