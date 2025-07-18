package com.example.joyrun.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.bean.RunningEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RunIndoorViewModel(private val dao: RunningEventDao) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var accumulatedDuration = 0L
    private var lastPauseTime = 0L
    private var lastSecond = -1

    private var stepCount = 0
    private val stepLength = 0.8f

    private var _runningEvent = MutableLiveData<RunningEvent>(RunningEvent())
    val runningEvent get() = _runningEvent

    private var _currentPace = MutableLiveData<String>("-'--\"")
    val currentPace get() = _currentPace

    private val _duration = MutableLiveData<Long>(0L)
    val duration get() = _duration

    private var _isRunning = false
    val isRunning get() = _isRunning

    private val updateRunnable = object : Runnable {
        override fun run() {
            val now = System.currentTimeMillis()
            val startTime = _runningEvent.value?.startTime ?: now
            val duration = now - startTime - accumulatedDuration
            _runningEvent.value?.duration = duration
            _runningEvent.value?.endTime = now

            val currentSecond = (duration / 1000).toInt()
            if (currentSecond != lastSecond) {
                lastSecond = currentSecond
                _duration.value = duration
                calculateRealTimePace()
            }

            _runningEvent.value = _runningEvent.value
            handler.postDelayed(this, 1000)
        }
    }

    fun onStepDetected() {
        if (!_isRunning) return
        stepCount++
        val distance = stepCount * stepLength
        _runningEvent.value?.totalDistance = distance
        calculateRealTimePace()
        _runningEvent.value = _runningEvent.value
    }

    fun startRunning() {
        _isRunning = true
        val now = System.currentTimeMillis()
        _runningEvent.value?.startTime = now
        _runningEvent.value?.endTime = now
        accumulatedDuration = 0
        stepCount = 0
        handler.post(updateRunnable)
    }

    fun pauseRunning() {
        _isRunning = false
        lastPauseTime = System.currentTimeMillis()
        handler.removeCallbacks(updateRunnable)
    }

    fun continueRunning() {
        _isRunning = true
        accumulatedDuration += System.currentTimeMillis() - lastPauseTime
        handler.post(updateRunnable)
    }

    fun stopRunning(): Boolean {
        _isRunning = false
        handler.removeCallbacks(updateRunnable)
        val totalTime = _runningEvent.value?.duration ?: 0L
        val totalDistance = _runningEvent.value?.totalDistance ?: 0f
        return if (totalTime > 5000 && totalDistance > 10f) {
            viewModelScope.launch(Dispatchers.IO) {
                dao.insert(_runningEvent.value ?: RunningEvent())
            }
            true
        } else {
            false
        }
    }

    // 计算实时配速
    private val paceWindow = mutableListOf<Pair<Long, Float>>()  // 时间戳，距离
    private fun calculateRealTimePace() {
        val now = System.currentTimeMillis()
        val currentDistance = _runningEvent.value?.totalDistance ?: 0f

        // 添加当前时间和距离
        paceWindow.add(now to currentDistance)

        // 移除5秒以前的数据，保证滑动窗口大小
        while (paceWindow.isNotEmpty() && now - paceWindow[0].first > 5000) {
            paceWindow.removeAt(0)
        }

        if (paceWindow.size < 2) {
            // 数据点不足显示无效配速
            _currentPace.value = "-'--\""
            return
        }

        // 时间差和距离差
        val timeDiff = (paceWindow.last().first - paceWindow.first().first) / 1000f
        val distDiff = paceWindow.last().second - paceWindow.first().second

        if (timeDiff <= 0f || distDiff <= 0f) {
            _currentPace.value = "-'--\""
            return
        }

        val speed = distDiff / timeDiff
        val paceMinPerKm = 1000f / speed / 60f
        val totalSec = (paceMinPerKm * 60).toInt()
        val min = totalSec / 60
        val sec = totalSec % 60
        _currentPace.value = String.format("%d'%02d\"", min, sec)
    }

}
