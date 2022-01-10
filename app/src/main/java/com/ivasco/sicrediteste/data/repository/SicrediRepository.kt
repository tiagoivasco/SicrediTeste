package com.ivasco.sicrediteste.data.repository

import com.ivasco.sicrediteste.data.SicrediAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SicrediRepository {
    fun makeRequest(): SicrediAPI {
        val baseUrl = "http://5f5a8f24d44d640016169133.mockapi.io/"

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SicrediAPI::class.java)
    }
}