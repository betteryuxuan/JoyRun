package com.example.joyrun.personalpage.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.joyrun.db.RunningEventDao
import com.example.module.libBase.utils.SPUtils
import java.time.LocalDate
import java.time.ZoneId

class PersonalRepository(private val dao: RunningEventDao, private val context: Context) {

    private val zoneId = ZoneId.systemDefault()
    private val today = LocalDate.now()

    private val startOfDay: Long = today.atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()
    private val endOfDay: Long = today.atTime(23, 59, 59).atZone(zoneId).toInstant().toEpochMilli()

    private val startOfMonth: Long = today.withDayOfMonth(1)
        .atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()
    private val endOfMonth: Long = today.withDayOfMonth(today.lengthOfMonth())
        .atTime(23, 59, 59).atZone(zoneId).toInstant().toEpochMilli()

    private val startOfYear: Long = today.withDayOfYear(1)
        .atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()
    private val endOfYear: Long = today.withDayOfYear(today.lengthOfYear())
        .atTime(23, 59, 59).atZone(zoneId).toInstant().toEpochMilli()

    fun getTotalDistance(): LiveData<Float> = dao.getTotalDistance()

    fun getYearDistance(): LiveData<Float> = dao.getTotalDistanceByTime(startOfYear, endOfYear)

    fun getMonthDistance(): LiveData<Float> = dao.getTotalDistanceByTime(startOfMonth, endOfMonth)

    fun getTodayDistance(): LiveData<Float> = dao.getTotalDistanceByTime(startOfDay, endOfDay)

    fun saveTargetMonthDistance(target: Int) {
        SPUtils.putInt(context, SPUtils.TARGET_MONTH_DISTANCE_KEY, target)
    }

    fun getTargetMonthDistance(): Int {
        return SPUtils.getInt(context, SPUtils.TARGET_MONTH_DISTANCE_KEY, 120)
    }

    fun saveTargetYearDistance(target: Int) {
        SPUtils.putInt(context, SPUtils.TARGET_YEAR_DISTANCE_KEY, target)
    }

    fun getTargetYearDistance(): Int {
        return SPUtils.getInt(context, SPUtils.TARGET_YEAR_DISTANCE_KEY, 1200)
    }

    fun saveUserName(userName: String) {
        SPUtils.putString(context, SPUtils.USER_NAME_KEY, userName)
    }

    fun getUserName(): String {
        return SPUtils.getString(context, SPUtils.USER_NAME_KEY, "冯雨轩")
    }
}
