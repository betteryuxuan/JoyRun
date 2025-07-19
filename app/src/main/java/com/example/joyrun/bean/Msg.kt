package com.example.joyrun.bean

data class Msg(
        val content: String,
        val time: String,
        val type: Int
) {
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
        const val TYPE_THINKING = 2
    }
}
