package com.example.chatgptapp.network

data class CompletionRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double,
    val max_tokens: Int
)

data class Message(
    val role: String,
    val content: String
)