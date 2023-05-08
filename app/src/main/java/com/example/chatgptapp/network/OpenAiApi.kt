import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Data classes
data class CompletionRequest(val model: String, val messages: List<Message>, val temperature: Double)
data class Message(val role: String, val content: String)
data class CompletionResponse(val id: String, val object: String, val choices: List<Choice>)
data class Choice(val message: Message)

// API Key Interceptor
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer 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")
            .build()
        return chain.proceed(newRequest)
    }
}

// API Service
interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun getCompletion(@Body completionRequest: CompletionRequest): retrofit2.Response<CompletionResponse>
}

// API setup
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor())
    .addInterceptor(loggingInterceptor)
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openai.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

val openAIApiService = retrofit.create(OpenAIApiService::class.java)

// Make the API call
suspend fun fetchCompletion(): CompletionResponse {
    val messages = listOf(Message(role = "user", content = "Tell me a joke"))
    val request = CompletionRequest(model = "gpt-3.5-turbo", messages = messages, temperature = 0.7)
    val response = openAIApiService.getCompletion(request)

    if (response.isSuccessful) {
        return response.body()!!
    } else {
        throw Exception("Error: ${response.code()} - ${response.message()}")
    }
}
