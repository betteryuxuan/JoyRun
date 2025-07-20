package com.example.module.libBase.utils

import android.content.Context

object SPUtils {
    private const val FILE_NAME = "PREFERENCE_NAME"
    const val MSGLIST_KEY: String = "msglist"

    fun putString(context: Context, key: String?, value: String?) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putString(key, value)
            .apply()
    }

    fun getString(context: Context, key: String?): String? {
        return getString(context, key, null)
    }

    fun getString(context: Context, key: String?, defaultValue: String?): String? {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getString(key, defaultValue)
    }


    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .clear()
            .apply()
    }

    fun clearMsgList(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            sp.edit()
                .remove(MSGLIST_KEY)
                .apply()
    }
}