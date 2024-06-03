package com.example.retrofitpractice.data

import com.example.retrofitpractice.model.CatFacts
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/fact")
    suspend fun getRandomFact(): Response<CatFacts>
}