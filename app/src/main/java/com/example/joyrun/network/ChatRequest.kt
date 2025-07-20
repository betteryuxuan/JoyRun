package com.example.joyrun.network

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false
)
