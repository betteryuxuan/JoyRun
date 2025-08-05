package com.example.joyrun.chatpage.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joyrun.bean.Msg
import com.example.joyrun.chatpage.model.ChatModel
import com.example.joyrun.utils.FormatUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(private val context: Context) : ViewModel() {
    private val model = ChatModel(context)
    private val _msgList = MutableLiveData<MutableList<Msg>>()
    val msgList: LiveData<MutableList<Msg>> = _msgList

    private val initMsg: Msg = Msg(
        "你好呀！欢迎来到小度的健身小天地！🌟 无论你是健身新手还是已经有一定经验的朋友，我都会用我的专业知识来帮助你，让你在运动和健康饮食的路上走得更顺畅、更快乐！",
        FormatUtils.getFormattedNowTime(),
        Msg.TYPE_RECEIVED
    )
    init {
        val data = model.getMsgLocal()
        _msgList.value = data ?: mutableListOf(initMsg)
    }

    private val _isRequesting = MutableLiveData(false)
    val isRequesting: LiveData<Boolean> get() = _isRequesting

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun sendUserMessage(content: String) {
        if (_isRequesting.value == true) {
            _error.value = "正在回答中，请稍后"
            return
        }

        val sendMsg = Msg(content, FormatUtils.getFormattedNowTime(), Msg.Companion.TYPE_SENT)
        _msgList.value?.add(sendMsg)
        _msgList.postValue(_msgList.value)
//        _msgList.value  =


        sendMessage(content)
    }

    private fun sendMessage(content: String) {
        viewModelScope.launch {
            _isRequesting.value = true
            try {
                val reply = withContext(Dispatchers.IO) {
                    model.sendChatRequest(content)
                }

                val replyMsg = Msg(reply, FormatUtils.getFormattedNowTime(), Msg.TYPE_RECEIVED)
                val newList = _msgList.value?.toMutableList() ?: mutableListOf()
                newList.add(replyMsg)
                _msgList.value = newList
            } catch (e: Exception) {
                _error.value = "请求失败：" + e.message
            } finally {
                _isRequesting.value = false
            }
        }

    }

    fun clearError() {
        _error.value = ""
    }

    fun saveToLocal() {
        model.saveToLocal(msgList.value)
    }

    fun openNewChat() {
        model.clearLocalMsg()
        _msgList.value = mutableListOf(initMsg)
    }
}