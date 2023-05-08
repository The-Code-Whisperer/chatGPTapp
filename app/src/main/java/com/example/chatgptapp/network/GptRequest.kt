package com.example.chatgptapp.network

import com.squareup.moshi.Json

data class 
ptRequest(
    @Json(name = "prompt") val prompt: String,
    @Json(name = "max_tokens") val maxTokens: Int,
    @Json(name = "temperature") val temperature: Float,
)
