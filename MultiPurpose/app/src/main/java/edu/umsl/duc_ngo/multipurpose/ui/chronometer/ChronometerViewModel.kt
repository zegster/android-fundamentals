package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChronometerViewModel : ViewModel() {
    private var mIsActive = MutableLiveData<Boolean>()

    init {
        mIsActive.value = false
    }

    fun getIsActive(): LiveData<Boolean> {
        return mIsActive
    }
//
//    fun setIsFetch(bool: Boolean) {
//        mIsFetch.value = bool
//    }
//
//    fun getJsonResult (): String {
//        return mJsonResult.value!!
//    }
//
//    fun setJsonResult (body: String) {
//        mJsonResult.value = body
//    }
}