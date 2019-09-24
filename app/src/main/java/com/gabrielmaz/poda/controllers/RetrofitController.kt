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
    var accessToken: String? = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJleHBpcmVzX29uIjoiMjAxOS0wOS0yNiAwMDo0Njo0MCArMDAwMCIsImV4cCI6MTU2OTQ1ODgwMX0.9JgVCRkCr2EQVEqjeYWGdrxR3TvaEzGj7DprpME6dmA"

    val retrofit= Retrofit.Builder()
        .baseUrl("https://android-todos-api.herokuapp.com/")
        .addConverterFactory(gsonConverterFactory)
        .client(httpClient)
        .build()

    private val gsonConverterFactory
        get() =GsonConverterFactory.create(
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
                        .addHeader("Authorization", accessToken)
                        .build()

                    chain.proceed(request)
                } else {
                    chain.proceed(chain.request())
                }
            }
            .build()
}