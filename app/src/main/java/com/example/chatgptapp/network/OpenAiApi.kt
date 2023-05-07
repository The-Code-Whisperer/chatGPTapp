package com.example.chatgptapp.network

import com.example.chatgptapp.ChatItem
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    @Headers("Content-Type: application/json")
    @POST("https://api.openai.com/v1/engines/davinci-codex/completions")
    suspend fun getGptResponse(@Body request: GptRequest): GptResponse
}
