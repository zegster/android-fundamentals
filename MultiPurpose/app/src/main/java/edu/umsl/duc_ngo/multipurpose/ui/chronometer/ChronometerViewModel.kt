package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChronometerViewModel : ViewModel() {
    enum class TimerState {
        Running, Paused, Stopped
    }

    private var mTimerState = MutableLiveData<TimerState>()
    private var mTimerInitLength = MutableLiveData<Long>()
    private var mTimerLength = MutableLiveData<Long>()
    private var mTimerRemaining = MutableLiveData<Long>()

    init {
        mTimerState.value = TimerState.Stopped
        mTimerInitLength.value = 0L
        mTimerLength.value = 0L
        mTimerRemaining.value = 0L
    }

    fun getTimerState(): TimerState {
        return mTimerState.value!!
    }

    fun setTimerState(state: TimerState) {
        mTimerState.value = state
    }

    fun getPreviousTimerLength(): Long {
        return mTimerLength.value!!
    }

    fun setPreviousTimerLength(seconds: Long) {
        mTimerLength.value = seconds
    }

    fun setTimerLength(seconds: Long) {
        mTimerInitLength.value = seconds
    }

    fun getTimerLength(): LiveData<Long> {
        return mTimerInitLength
    }

    fun getTimerRemaining(): Long {
        return mTimerRemaining.value!!
    }

    fun setTimerRemaining(seconds: Long) {
        mTimerRemaining.value = seconds
    }
}