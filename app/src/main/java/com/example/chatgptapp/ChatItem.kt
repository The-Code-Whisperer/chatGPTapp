package com.example.chatgptapp

data class ChatItem(
    val message: String,
    val type: ChatItemType
)

enum class ChatItemType {
    USER_MESSAGE,
    GPT_RESPONSE
}
