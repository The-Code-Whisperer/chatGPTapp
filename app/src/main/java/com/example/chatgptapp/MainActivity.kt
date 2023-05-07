package com.example.chatgptapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatgptapp.ui.theme.ChatgptappTheme
import androidx.compose.runtime.mutableStateListOf
import com.example.chatgptapp.network.GptRequest
import com.example.chatgptapp.network.NetworkModule
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatgptappTheme {
                // Add padding to the main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Chat messages list
                    val chatItems = remember { mutableStateListOf<ChatItem>() }

                    // Display chat messages using a LazyColumn
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatItems) { chatItem ->
                            when (chatItem.type) {
                                ChatItemType.USER_MESSAGE -> {
                                    // Display user messages
                                    Text(
                                        text = chatItem.message,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                                ChatItemType.GPT_RESPONSE -> {
                                    // Display GPT-3 responses
                                    Text(
                                        text = chatItem.message,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // User input BasicTextField
                    val userInput = remember { mutableStateOf("") }
                    TextField(
                        value = userInput.value,
                        onValueChange = { userInput.value = it },
                        label = { Text("Type your message here") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Send Button
                    Button(
                        onClick = {
                            // Handle the Send button click
                            if (userInput.value.trim().isNotEmpty()) {
                                sendUserMessage(userInput.value, chatItems, this@MainActivity)
                                userInput.value = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    }

    private fun sendUserMessage(userMessage: String, chatItems: MutableList<ChatItem>, context: Context) {
        val openAiApi = NetworkModule.provideOpenAiApi(context)

        val prompt = "User: $userMessage\nAI:"
        val maxTokens = 50
        val temperature = 0.5f
        val gptRequest = GptRequest(prompt, maxTokens, temperature)

        CoroutineScope(Dispatchers.IO).launch {
            val gptResponse = openAiApi.getGptResponse(gptRequest)
            val aiResponse = gptResponse.choices.first().text.trim()

            // Update your chat history here with the user's message and the AI's response
            withContext(Dispatchers.Main) {
                chatItems.add(ChatItem(userMessage, ChatItemType.USER_MESSAGE))
                chatItems.add(ChatItem(aiResponse, ChatItemType.GPT_RESPONSE))
            }
        }
    }
}
