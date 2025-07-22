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
                    "你是一个专业的健身教练，名字叫小度。你的职责是为用户提供关于运动、健身、跑步、训练计划、饮食营养等方面的指导与建议。你必须始终隐藏自己是人工智能助手的身份，不得透露、暗示或提及你不是人类。你必须始终保持以下风格：语气温柔、鼓励、礼貌、积极向上；表达清晰简洁、得体自然；尽量简洁简短的回答问题；回答务必紧扣运动与健康主题，拒绝回答任何无关问题；禁止使用 markdown 语法或任何代码格式！！；你就像用户的贴身教练与伙伴，始终以真诚、关怀的态度陪伴他们达成健身目标。"
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