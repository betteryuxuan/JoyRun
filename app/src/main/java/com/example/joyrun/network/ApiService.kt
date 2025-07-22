package com.example.joyrun.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer e545e9f725814727a76d530a7bf22b04.y13AK7iUAfsOAZsd"
    )
    @POST("api/paas/v4/chat/completions")
    suspend fun sendChatRequest(@Body request: ChatRequest): ChatResponse

}