package com.example.joyrun.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.AMapUtils
import com.amap.api.maps2d.model.LatLng
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.DAO.RunningEventDatabase
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.model.CountDownModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownViewModel(private val dao: RunningEventDao) : ViewModel() {
    private var totalDistance: Float = 0.00f
    private val model = CountDownModel(dao)

    private var _isRunning = false
    val isRunning get() = _isRunning

    private var _runningEvent = MutableLiveData<RunningEvent>(RunningEvent())// 跑步事件初始化
    val runningEvent get() = _runningEvent

    private var _currentPace = MutableLiveData<String>("-'--\"") // 实时配速（min/km）
    val currentPace get() = _currentPace

    private val _duration = MutableLiveData<Long>(0L)
    val duration get() = _duration

    private var accumulatedDuration = 0L // 暂停时长
    private var lastPauseTime = 0L // 上一次暂停的时间戳
    private var lastSecond = -1
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            val runningEvent = _runningEvent.value ?: return
            val startTime = runningEvent.startTime ?: currentTime

            val duration = currentTime - startTime - accumulatedDuration
            runningEvent.duration = duration
            runningEvent.endTime = currentTime
            _runningEvent.value = runningEvent

            val currentSecond = (duration / 1000).toInt()
            if (currentSecond != lastSecond) {
                lastSecond = currentSecond
                _duration.value = duration
            }

            handler.postDelayed(this, 1000)
        }
    }

    fun startRunning() {
        _isRunning = true
        val currentTime = System.currentTimeMillis()
        _runningEvent.value?.startTime = currentTime
        _runningEvent.value?.endTime = currentTime
        accumulatedDuration = 0L
        handler.post(updateRunnable)
    }

    fun pauseRunning() {
        _isRunning = false
        lastPauseTime = System.currentTimeMillis()
        handler.removeCallbacks(updateRunnable)
    }

    fun continueRunning() {
        _isRunning = true
        val currentTime = System.currentTimeMillis()
        val pausedDuration = currentTime - lastPauseTime
        accumulatedDuration += pausedDuration
        handler.post(updateRunnable)
    }

    fun stopRunning(): Boolean {
        _isRunning = false
        handler.removeCallbacks(updateRunnable)
        _runningEvent.value?.endTime = System.currentTimeMillis()
        _runningEvent.value?.duration = _duration.value ?: 0
        // 存储数据
        // 距离小于10米，持续时间小于5秒不保存
        if ((_runningEvent.value?.totalDistance ?: 0f) > 10f
            && (_runningEvent.value?.duration ?: 0) > 5000
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                model.insert(_runningEvent.value ?: RunningEvent())
            }
            return  true
        } else {
            return false
        }

    }

    fun handleLocationUpdate(amapLocation: AMapLocation) {
        val latLng = LatLng(amapLocation.latitude, amapLocation.longitude)

        // 计算距离
        if (_runningEvent.value?.pathPoints?.isNotEmpty() == true && _isRunning) {
            val lastLatLng = _runningEvent.value?.pathPoints?.last()
            val distance = AMapUtils.calculateLineDistance(lastLatLng, latLng)
            totalDistance += distance
            _runningEvent.value?.totalDistance = totalDistance
        }

        // 添加新点
        _runningEvent.value?.pathPoints?.add(latLng)
        // 计算配速
        calculatePace(amapLocation.speed)

        // 更新UI
        _runningEvent.value = _runningEvent.value
    }

    private fun calculatePace(speed: Float) {
        _currentPace.value = if (!_isRunning) {
            "--'--\""
        } else if (speed != 0f) {
            val paceMinPerKm = 16.666f / speed
            val totalSec = (paceMinPerKm * 60).toInt()
            val min = totalSec / 60
            val sec = totalSec % 60
            String.format("%d'%02d\"", min, sec)
        } else {
            "0'00\""
        }
    }


}
