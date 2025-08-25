package com.example.postly.Model.DataSource.Remote.API

import com.example.postly.Model.DataSource.Remote.DTO.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNewsArticles(
        @Query("q") query: String = "technology",
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): NewsApiResponse
}