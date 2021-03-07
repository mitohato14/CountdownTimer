package com.example.androiddevchallenge.ui.countdown

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by mitohato14 on 2021/03/06.
 */
class CountdownViewModel : ViewModel() {
    private val remainingTime: MutableLiveData<Int> = MutableLiveData(0)
    
    val remainingMinute: LiveData<String> = remainingTime.map {
        String.format("%02d", it / 60)
    }
    val remainingSecond: LiveData<String> = remainingTime.map {
        String.format("%02d", it % 60)
    }
    
    private val _settingMinute: MutableLiveData<String> = MutableLiveData("00")
    val settingMinute: LiveData<String>
        get() = _settingMinute
    
    private val _settingSecond: MutableLiveData<String> = MutableLiveData("00")
    val settingSecond: LiveData<String>
        get() = _settingSecond
    
    private var countdownJob: Job? = null
    
    var countStarted: Boolean = false
        private set
    
    var isPaused: Boolean = false
        private set
    
    
    fun onSettingMinuteChange(minute: String) {
        _settingMinute.value = minute
    }
    
    fun onSettingSecondChange(second: String) {
        _settingSecond.value = second
    }
    
    fun onClickStart() {
        countStarted = true
        val snapshot = isPaused
        isPaused = false
        if (snapshot) {
            onRestartTimer()
        } else {
            setRemainingTime()
            startCountdownTimer()
        }
    }
    
    fun onStopTimer() {
        countStarted = false
        isPaused = false
        setRemainingTime()
        countdownJob?.cancel()
    }
    
    fun onPauseTimer() {
        countStarted = false
        isPaused = true
        countdownJob?.cancel()
    }
    
    private fun onRestartTimer() {
        countStarted = true
        isPaused = false
        startCountdownTimer()
    }
    
    private fun setRemainingTime() {
        remainingTime.value = requireNotNull(_settingMinute.value).toInt() * 60 +
                requireNotNull(_settingSecond.value).toInt()
    }
    
    private fun startCountdownTimer() {
        countdownJob = viewModelScope.launch {
            while (true) {
                if (remainingTime.value == 0) {
                    break
                }
                delay(1000L)
                remainingTime.value = remainingTime.value ?: 0 - 1
            }
        }
    }
    
}
