package com.example.postly.Model.DataSource.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.postly.Model.DataSource.Local.Entity.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>): List<Long>

    @Update
    suspend fun updatePost(post: PostEntity): Int


    @Update
    suspend fun updatePosts(posts: List<PostEntity>): Int


    @Delete
    suspend fun deletePost(post: PostEntity): Int


    @Delete
    suspend fun deletePosts(posts: List<PostEntity>): Int

    @Query("SELECT * FROM posts ORDER BY id ASC")
    suspend fun getAllPosts(): List<PostEntity>

    @Query("SELECT id FROM posts")
    suspend fun getAllPostIds(): List<Int>

    @Query("DELETE FROM posts WHERE id NOT IN (:keepIds) AND isFavorite = 0")
    suspend fun deletePostsNotInListExceptFavorites(keepIds: List<Int>): Int

    @Query("SELECT * FROM posts ORDER BY title ASC")
    suspend fun getAllPostsOrderedByTitle(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?


    @Query("SELECT * FROM posts WHERE LOWER(title) LIKE LOWER('%' || :title || '%') ORDER BY title ASC")
    suspend fun getPostsByTitle(title: String): List<PostEntity>

    @Query("SELECT * FROM posts WHERE LOWER(body) LIKE LOWER('%' || :body || '%') ORDER BY id ASC")
    suspend fun getPostsByBody(body: String): List<PostEntity>

    @Query("SELECT * FROM posts WHERE isFavorite = 1 ORDER BY id ASC")
    suspend fun getFavoritePosts(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE isFavorite = 0 ORDER BY id ASC")
    suspend fun getNonFavoritePosts(): List<PostEntity>

    @Query("UPDATE posts SET isFavorite = NOT isFavorite WHERE id = :postId")
    suspend fun toggleFavorite(postId: Int): Int

    @Query("UPDATE posts SET isFavorite = :isFavorite WHERE id = :postId")
    suspend fun setFavorite(postId: Int, isFavorite: Boolean): Int

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostsCount(): Int

    @Query("SELECT COUNT(*) FROM posts WHERE isFavorite = 1")
    suspend fun getFavoritePostsCount(): Int

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts(): Int

    @Query("DELETE FROM posts WHERE id IN (:postIds)")
    suspend fun deletePostsByIds(postIds: List<Int>): Int

    @Query("SELECT * FROM posts WHERE LOWER(title) LIKE LOWER('%' || :query || '%') OR LOWER(body) LIKE LOWER('%' || :query || '%') ORDER BY id ASC")
    suspend fun searchPosts(query: String): List<PostEntity>

    @Query("SELECT * FROM posts ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPostsWithPagination(limit: Int, offset: Int): List<PostEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM posts WHERE id = :postId)")
    suspend fun postExists(postId: Int): Boolean
}