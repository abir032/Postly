package com.example.postly.Model.DataSource.Remote.API

import com.example.postly.Model.DataSource.Remote.DTO.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsArticles(
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): NewsApiResponse
}