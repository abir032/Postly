package com.example.postly.Model.Repository.Contracts.Post

import com.example.postly.Model.DataModels.Post
import com.example.postly.Model.DataModels.Result

interface IFPostRepository : IFGetPostRepository, IFGetFavoritePostRepository, IFUpdateFavouritePostRepository, IFSearchPostRepository {
}

interface IFGetPostRepository {
    suspend fun getPosts(page: Int = 1, pageSize: Int = 20): Result<List<Post>>
}

interface IFGetFavoritePostRepository {
    suspend fun getFavoritePosts(): Result<List<Post>>
}

interface IFUpdateFavouritePostRepository {
    suspend fun toggleFavorite(postId: Int): Result<Unit>
}

interface  IFSearchPostRepository {
    suspend fun searchPosts(query: String): Result<List<Post>>
}