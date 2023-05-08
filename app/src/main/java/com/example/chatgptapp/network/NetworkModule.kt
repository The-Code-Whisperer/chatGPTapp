package com.example.chatgptapp.network

import android.content.Context
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.openai.com/v1/"

    // Define a new function that returns an instance of OkHttpClient
    private fun provideHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Add your custom interceptor here
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private fun provideRetrofit(context: Context): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient(context)) // Use the new function here
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun provideOpenAiApi(context: Context): OpenAiApi {
        return provideRetrofit(context).create(OpenAiApi::class.java)
    }
}

// Define a new class that implements the Interceptor interface
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()

        // Add your authorization header here
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer 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")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
