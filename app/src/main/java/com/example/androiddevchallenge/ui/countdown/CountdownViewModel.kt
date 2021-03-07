/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.countdown

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.toInt
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

    private val _settingMinute: MutableLiveData<String> = MutableLiveData("")
    val settingMinute: LiveData<String>
        get() = _settingMinute

    private val _settingSecond: MutableLiveData<String> = MutableLiveData("")
    val settingSecond: LiveData<String>
        get() = _settingSecond

    private var settingTime: Int = 0

    private var countdownJob: Job? = null

    private val _countStarted: MutableLiveData<Boolean> = MutableLiveData(false)
    val countStarted: LiveData<Boolean>
        get() = _countStarted

    private var isPaused = false

    private val _timeProgress: MutableLiveData<Float> = MutableLiveData(0f)
    val timeProgress: LiveData<Float>
        get() = _timeProgress

    fun onSettingMinuteChange(minute: String) {
        val newMinute = minute.toInt(default = 0).coerceIn(0..99).toString()
        _settingMinute.value = if (newMinute == "0") "" else newMinute
    }

    fun onSettingSecondChange(second: String) {
        val newSecond = second.toInt(default = 0).coerceIn(0..99).toString()
        _settingSecond.value = newSecond
    }

    fun onClickStart() {
        if (_settingMinute.value == "" && _settingSecond.value == "") return

        _countStarted.value = true
        val snapshot = isPaused
        isPaused = false
        if (snapshot) {
            restartTimer()
        } else {
            _timeProgress.value = 1f
            setRemainingTime()
            startCountdownTimer()
        }
    }

    fun onStopTimer() {
        _countStarted.value = false
        isPaused = false
        resetTime()
        _timeProgress.value = 0f
        countdownJob?.cancel()
    }

    fun onPauseTimer() {
        _countStarted.value = false
        isPaused = true
        _settingMinute.value = remainingMinute.value
        _settingSecond.value = remainingSecond.value
        countdownJob?.cancel()
    }

    private fun restartTimer() {
        _countStarted.value = true
        isPaused = false
        startCountdownTimer()
    }

    private fun setRemainingTime() {
        settingTime = requireNotNull(_settingMinute.value).toInt(default = 0) * 60 +
            requireNotNull(_settingSecond.value).toInt(default = 0)
        remainingTime.value = settingTime
    }

    private fun resetTime() {
        remainingTime.value = settingTime
        _settingMinute.value = remainingMinute.value
        _settingSecond.value = remainingSecond.value
    }

    private fun startCountdownTimer() {
        countdownJob = viewModelScope.launch {
            while (true) {
                val remainingTimeSnap = remainingTime.value ?: 0
                if (remainingTimeSnap == 0) {
                    _countStarted.value = false
                    isPaused = false
                    break
                }
                delay(1000L)
                val newRemainingTime = remainingTimeSnap - 1
                remainingTime.value = newRemainingTime
                _timeProgress.value = newRemainingTime.toFloat() / settingTime.toFloat()
            }

            resetTime()
        }
    }
}
