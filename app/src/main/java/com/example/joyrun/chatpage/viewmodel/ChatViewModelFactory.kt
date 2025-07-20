package com.example.joyrun.chatpage.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatViewModelFactory constructor(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java))
            return ChatViewModel(context) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}