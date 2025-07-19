package com.example.joyrun.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amap.api.maps2d.model.LatLng

@Entity
data class RunningEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var runType: Int = 0, // 运动类型，0室内，1室外
    var startTime: Long ?= null,             // 跑步开始时间
    var endTime: Long? = null,                // 跑步结束时间
    var totalDistance: Float = 0f,         // 总距离（米）
    var duration: Long = 0L,                      // 真实运动时长
    var pathPoints: MutableList<LatLng> = mutableListOf(), // 路线点
    var calories: Int = 0,                        // 消耗卡路里
    var avgSpeed: Float = 0f                      // 平均速度 km/h
)
