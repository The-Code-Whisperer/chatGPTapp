package com.example.chatgptapp.network

import com.squareup.moshi.Json

data class GptResponse(
    @Json(name = "id") val id: String,
    @Json(name = "object") val objectName: String,
    @Json(name = "created") val created: Int,
    @Json(name = "model") val model: String,
    @Json(name = "usage") val usage: Usage,
    @Json(name = "choices") val choices: List<Choice>
) {
    data class Usage(
        @Json(name = "prompt_tokens") val promptTokens: Int,
        @Json(name = "completion_tokens") val completionTokens: Int,
        @Json(name = "total_tokens") val totalTokens: Int
    )

    data class Choice(
        @Json(name = "text") val text: String,
        @Json(name = "index") val index: Int,
        @Json(name = "logprobs") val logprobs: Any?,
        @Json(name = "finish_reason") val finishReason: String
    )
}
