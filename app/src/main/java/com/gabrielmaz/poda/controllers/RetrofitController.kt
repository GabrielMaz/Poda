package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.App
import com.gabrielmaz.poda.controllers.adapter.ZonedDateTimeAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitController {
        val baseUrl = "https://android-todos-api.herokuapp.com"
//        val baseUrl = "https://android-todos-api.herokuapp.com"
        var accessToken: String? = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3LCJleHBpcmVzX29uIjoiMjAxOS0wOS0yOSAwMDo1NzowNSArMDAwMCIsImV4cCI6MTU2OTcxODYyNn0._jOvZRTgvGR797VOYJtF7-RWDRi4RVhw2NKe3KWhkZk"
//    var accessToken: String? = null

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .client(httpClient)
        .build()

    private val gsonConverterFactory
        get() = GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
                .create()
        )

    private val httpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())

                if (response.code() in 400..499) {
                    App.goToLoginScreen()
                }

                response
            }
            .addInterceptor { chain ->
                if (accessToken != null) {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", accessToken ?: "")
                        .build()

                    chain.proceed(request)
                } else {
                    chain.proceed(chain.request())
                }
            }
            .build()
}