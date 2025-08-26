package com.example.postly.Model.Repository.Repositories.PostRepositories

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.postly.Config.ApiConfig
import com.example.postly.Model.DataModels.Post
import com.example.postly.Model.DataModels.Result
import com.example.postly.Model.Repository.Contracts.Post.IFPostRepository
import com.example.postly.Model.DataSource.Local.DAO.PostDao
import com.example.postly.Model.DataSource.Local.Entity.PostEntity
import com.example.postly.Model.DataSource.Remote.API.ApiService
import com.example.postly.Utils.AppError
import com.example.postly.Utils.NetworkManager
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val apiService: ApiService,
    private val networkManager: NetworkManager
) : IFPostRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getPosts(page: Int, pageSize: Int): Result<List<Post>> {
        return try {
            val cachedPosts = getCachedPosts(page, pageSize)

            if (networkManager.isNetworkAvailable()) {
                try {
                    val apiResponse = apiService.getNewsArticles(
                        apiKey = ApiConfig.API_KEY,
                        page = page,
                        pageSize = pageSize
                    )
                    if (apiResponse.status == "ok") {
                        val postEntities = apiResponse.articles.mapIndexed { index, article ->
                            val postId = ((page - 1) * pageSize) + index
                            PostEntity(
                                id = postId,
                                title = article.title,
                                body = article.description ?: article.content ?: "",
                                isFavorite = isPostFavorite(postId)
                            )
                        }
                        if (page == 1) {
                            updateCacheWithFavoritePreservation(postEntities)
                        } else {
                            postDao.insertPosts(postEntities)
                        }

                        return Result.Success(getCachedPosts(page, pageSize))
                    } else {
                        return Result.Success(cachedPosts)
                    }
                } catch (e: Exception) {
                    Log.e("PostRepository", "Network error: ${e.message}")
                    return Result.Success(cachedPosts)
                }
            } else {
                return Result.Success(cachedPosts)
            }

        } catch (e: Exception) {
            handleApiError(e)
        }
    }

    override suspend fun getFavoritePosts(): Result<List<Post>> {
        return try {
            val favoriteEntities = postDao.getFavoritePosts()
            val favoritePosts = favoriteEntities.mapToPosts()
            Result.Success(favoritePosts)
        } catch (e: Exception) {
            Result.Error(AppError.DatabaseConnectionError)
        }
    }

    override suspend fun toggleFavorite(postId: Int): Result<Unit> {
        return try {
            val rowsAffected = postDao.toggleFavorite(postId)
            if (rowsAffected > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(AppError.CustomError(
                    code = "DB_003",
                    userMessage = "Failed to update favorite status"
                ))
            }
        } catch (e: Exception) {
            Result.Error(AppError.DatabaseConnectionError)
        }
    }

    override suspend fun searchPosts(query: String): Result<List<Post>> {
        return try {
            val searchResults = postDao.searchPosts(query).mapToPosts()
            Result.Success(searchResults)
        } catch (e: Exception) {
            Result.Error(AppError.DatabaseConnectionError)
        }
    }

    private suspend fun getCachedPosts(page: Int = 1, pageSize: Int = 20): List<Post> {
        val offset = (page - 1) * pageSize
        return postDao.getPostsWithPagination(pageSize, offset).mapToPosts()
    }

    private suspend fun isPostFavorite(postId: Int): Boolean {
        return postDao.getPostById(postId)?.isFavorite ?: false
    }

    private suspend fun updateCacheWithFavoritePreservation(newPosts: List<PostEntity>) {
        val favoritePostIds = postDao.getFavoritePosts().map { it.id }
        postDao.deletePostsNotInListExceptFavorites(newPosts.map { it.id })
        postDao.insertPosts(newPosts)
        favoritePostIds.forEach { favoriteId ->
            postDao.setFavorite(favoriteId, true)
        }
    }

    private suspend fun handleApiError(e: Exception): Result<List<Post>> {
        return when {
            e is java.net.UnknownHostException || e is java.net.ConnectException -> {
                Result.Success(getCachedPosts()).also {
                }
            }
            e is retrofit2.HttpException -> {
                Result.Error(AppError.CustomError(
                    code = "HTTP_${e.code()}",
                    userMessage = "Server error occurred",
                    debugMessage = e.message
                ))
            }
            else -> {
                Result.Error(AppError.CustomError(
                    code = "UNKNOWN_001",
                    userMessage = "An unexpected error occurred",
                    debugMessage = e.message
                ))
            }
        }
    }

    private fun List<PostEntity>.mapToPosts(): List<Post> {
        return this.map { entity ->
            Post(
                id = entity.id,
                title = entity.title,
                body = entity.body,
                isFavorite = entity.isFavorite
            )
        }
    }
}