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

    // Insert a single post
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    // Insert multiple posts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>): List<Long>

    // Update a single post
    @Update
    suspend fun updatePost(post: PostEntity): Int

    // Update multiple posts
    @Update
    suspend fun updatePosts(posts: List<PostEntity>): Int

    // Delete a single post
    @Delete
    suspend fun deletePost(post: PostEntity): Int

    // Delete multiple posts
    @Delete
    suspend fun deletePosts(posts: List<PostEntity>): Int

    // Get all posts ordered by ID
    @Query("SELECT * FROM posts ORDER BY id ASC")
    suspend fun getAllPosts(): List<PostEntity>

    @Query("SELECT id FROM posts")
    suspend fun getAllPostIds(): List<Int>

    // Delete posts that are not in the provided list AND not favorites
    @Query("DELETE FROM posts WHERE id NOT IN (:keepIds) AND isFavorite = 0")
    suspend fun deletePostsNotInListExceptFavorites(keepIds: List<Int>): Int

    // Get all posts ordered by title
    @Query("SELECT * FROM posts ORDER BY title ASC")
    suspend fun getAllPostsOrderedByTitle(): List<PostEntity>

    // Get a specific post by ID
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?

    // Get posts by title (case-insensitive search)
    @Query("SELECT * FROM posts WHERE LOWER(title) LIKE LOWER('%' || :title || '%') ORDER BY title ASC")
    suspend fun getPostsByTitle(title: String): List<PostEntity>

    // Get posts by body content (case-insensitive search)
    @Query("SELECT * FROM posts WHERE LOWER(body) LIKE LOWER('%' || :body || '%') ORDER BY id ASC")
    suspend fun getPostsByBody(body: String): List<PostEntity>

    // Get favorite posts
    @Query("SELECT * FROM posts WHERE isFavorite = 1 ORDER BY id ASC")
    suspend fun getFavoritePosts(): List<PostEntity>

    // Get non-favorite posts
    @Query("SELECT * FROM posts WHERE isFavorite = 0 ORDER BY id ASC")
    suspend fun getNonFavoritePosts(): List<PostEntity>

    // Toggle favorite status for a post
    @Query("UPDATE posts SET isFavorite = NOT isFavorite WHERE id = :postId")
    suspend fun toggleFavorite(postId: Int): Int

    // Set favorite status for a post
    @Query("UPDATE posts SET isFavorite = :isFavorite WHERE id = :postId")
    suspend fun setFavorite(postId: Int, isFavorite: Boolean): Int

    // Get posts count
    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostsCount(): Int

    // Get favorite posts count
    @Query("SELECT COUNT(*) FROM posts WHERE isFavorite = 1")
    suspend fun getFavoritePostsCount(): Int

    // Delete all posts
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts(): Int

    // Delete posts by IDs
    @Query("DELETE FROM posts WHERE id IN (:postIds)")
    suspend fun deletePostsByIds(postIds: List<Int>): Int

    // Search posts by title or body
    @Query("SELECT * FROM posts WHERE LOWER(title) LIKE LOWER('%' || :query || '%') OR LOWER(body) LIKE LOWER('%' || :query || '%') ORDER BY id ASC")
    suspend fun searchPosts(query: String): List<PostEntity>

    // Get posts with pagination
    @Query("SELECT * FROM posts ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPostsWithPagination(limit: Int, offset: Int): List<PostEntity>

    // Check if a post exists
    @Query("SELECT EXISTS(SELECT 1 FROM posts WHERE id = :postId)")
    suspend fun postExists(postId: Int): Boolean
}