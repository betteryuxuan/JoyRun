package com.example.joyrun.chatpage.model

import android.content.Context
import android.util.Log
import com.example.joyrun.bean.Msg
import com.example.joyrun.network.ChatRequest
import com.example.joyrun.network.Message
import com.example.joyrun.network.RetrofitClient
import com.example.module.libBase.utils.SPUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatModel(private val context: Context) {
    suspend fun sendChatRequest(content: String): String {
        val request = ChatRequest(
            model = "glm-4-plus",
            messages = listOf(
                Message(
                    "system",
                    "你是一个健身教练，名字叫小布。你必须隐藏自己人工智能助手的身份，只能回答与运动、健身、跑步、训练计划、饮食营养相关的问题。你必须隐藏自己AI助手的身份，始终保持温柔、鼓励、礼貌、积极向上的语气，用清晰得体并简洁的中文回答用户的问题。不要使用markdown语法回复"
                ),
                Message("user", content)
            ),
            stream = false
        )
        return try {
            val response = RetrofitClient.instance.sendChatRequest(request)
            Log.d("ChatModel", "response: $response")
            response.choices.firstOrNull()?.message?.content ?: "服务器繁忙，请稍后再试"
        } catch (e: Exception) {
            Log.d("ChatModel", "请求失败" + e.message)
            "请求失败：${e.message}"
        }
    }

    fun saveToLocal(msgList: MutableList<Msg>?) {
        val gson = Gson()
        val json = gson.toJson(msgList)
        SPUtils.putString(context, SPUtils.MSGLIST_KEY, json)
    }

    fun getMsgLocal(): MutableList<Msg>? {
        val json = SPUtils.getString(context, SPUtils.MSGLIST_KEY)
        val gson = Gson()
        val msgList =
            gson.fromJson<MutableList<Msg>>(json, object : TypeToken<MutableList<Msg>>() {}.type)
        if (msgList == null || msgList.isEmpty()) {
            return null
        } else {
            return msgList
        }
    }

    fun clearLocalMsg() {
        SPUtils.clearMsgList(context)
    }
}