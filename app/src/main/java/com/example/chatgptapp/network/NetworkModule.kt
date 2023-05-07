package com.example.chatgptapp.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.openai.com/"

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    fun provideOpenAiApi(context: Context): OpenAiApi {
        return provideRetrofit().create(OpenAiApi::class.java)
    }
}
