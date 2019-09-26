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
        var accessToken: String? = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3LCJleHBpcmVzX29uIjoiMjAxOS0wOS0yNyAwMzoyNzo1OCArMDAwMCIsImV4cCI6MTU2OTU1NDg3OX0.A2SGFT7HhvCl-gEhy7rGEjvj-K2L14zFAtfhx-rItrs"
//    var accessToken: String? = null

    val retrofit = Retrofit.Builder()
        .baseUrl("https://android-todos-api.herokuapp.com")
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