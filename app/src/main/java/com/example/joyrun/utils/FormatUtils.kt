package com.example.joyrun.utils

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.floor

object FormatUtils {

    // 时间戳转日期字符串
    @JvmStatic
    fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return "未知时间"
        val date = Date(timestamp)
        val format = SimpleDateFormat("M月dd日 HH:mm", Locale.getDefault())
        return format.format(date)
    }

    // 距离（米）转为字符串（公里保留两位小数）
    @JvmStatic
    fun formatDistance(distanceInMeters: Float): String {
        val km = distanceInMeters / 1000f
        return String.format("%.2f 公里", km)
    }

    @JvmStatic
    fun formatDistanceWithoutkm(distanceInMeters: Float): String {
        val km = distanceInMeters / 1000f
        return String.format("%.2f", km)
    }


    // 时长（毫秒）转为 hh:mm:ss 格式
    @JvmStatic
    fun formatDuration(duration: Long): String {
        val durationInSeconds = duration / 1000
        val hours = TimeUnit.SECONDS.toHours(durationInSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) % 60
        val seconds = durationInSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // 时长（毫秒）转为 hh:mm:ss 格式
    @JvmStatic
    fun formatDurationWithoutHour(duration: Long): String {
        val durationInSeconds = duration / 1000
        val minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds)
        val seconds = durationInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // 平均速度（公里/小时），保留一位小数
    @JvmStatic
    fun calculateAvgSpeed(totalDistance: Float, durationInSeconds: Long): Float {
        if (durationInSeconds == 0L) return 0f
        val hours = durationInSeconds / 3600f
        return totalDistance / 1000f / hours
    }

    // 平均配速（分钟/公里）格式："mm'ss"
    @JvmStatic
    fun formatPace(totalDistance: Float, duration: Long): String {
        if (totalDistance == 0f || duration == 0L) return "--'--\""
        val paceInSecondsPerKm = (duration / 1000f) / (totalDistance / 1000f)
        val minutes = floor(paceInSecondsPerKm / 60).toInt()
        val seconds = (paceInSecondsPerKm % 60).toInt()
        if (minutes > 9999 || (minutes == 0 && seconds == 0))
            return "--'--\""
        return String.format("%2d'%2d\"", minutes, seconds)
    }

    fun getFormattedNowTime(): String {
        val formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.CHINA)
        return LocalTime.now().format(formatter)
    }
}
