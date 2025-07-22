package com.example.module.libBase.utils

import android.content.Context

object SPUtils {
    private const val FILE_NAME = "PREFERENCE_NAME"
    const val MSGLIST_KEY: String = "msgListKey"
    const val TARGET_MONTH_DISTANCE_KEY: String = "monthTargetDistanceKey"
    const val TARGET_YEAR_DISTANCE_KEY: String = "yearTargetDistanceKey"
    const val USER_NAME_KEY: String = "userNameKey"

    // 存 String
    fun putString(context: Context, key: String?, value: String?) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putString(key, value)
            .apply()
    }

    // 取 String（默认值为 null）
    fun getString(context: Context, key: String?): String? {
        return getString(context, key, null)
    }

    // 取 String（可指定默认值）
    fun getString(context: Context, key: String?, defaultValue: String?): String {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getString(key, defaultValue) ?: defaultValue.orEmpty()
    }

    fun putInt(context: Context, key: String?, value: Int) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putInt(key, value)
            .apply()
    }

    fun getInt(context: Context, key: String?): Int {
        return getInt(context, key, 0)
    }

    fun getInt(context: Context, key: String?, defaultValue: Int): Int {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getInt(key, defaultValue)
    }

    // 清空所有数据
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .clear()
            .apply()
    }

    // 清除消息列表
    fun clearMsgList(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .remove(MSGLIST_KEY)
            .apply()
    }
}
