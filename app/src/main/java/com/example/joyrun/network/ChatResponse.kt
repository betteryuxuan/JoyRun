package com.example.joyrun.network

data class ChatResponse(
    val id: String,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val finish_reason: String,
    val message: Message
)
