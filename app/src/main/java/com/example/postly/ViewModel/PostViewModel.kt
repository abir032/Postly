package com.example.postly.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.Model.DataModels.Post
import com.example.postly.Model.DataModels.Result
import com.example.postly.Model.Repository.Contracts.Post.IFPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: IFPostRepository
) : ViewModel() {

    // State for all posts
    private val _postsState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val postsState: StateFlow<Result<List<Post>>> = _postsState.asStateFlow()

    // State for favorite posts
    private val _favoritePostsState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val favoritePostsState: StateFlow<Result<List<Post>>> = _favoritePostsState.asStateFlow()

    // State for search results
    private val _searchState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val searchState: StateFlow<Result<List<Post>>> = _searchState.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPosts()
        loadFavoritePosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _postsState.value = Result.Loading

            when (val result = postRepository.getPosts()) {
                is Result.Success -> {
                    _postsState.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _postsState.value = Result.Error(result.error)
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun loadFavoritePosts() {
        viewModelScope.launch {
            _favoritePostsState.value = Result.Loading

            when (val result = postRepository.getFavoritePosts()) {
                is Result.Success -> {
                    _favoritePostsState.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _favoritePostsState.value = Result.Error(result.error)
                }
                else -> {}
            }
        }
    }

    fun toggleFavorite(postId: Int) {
        viewModelScope.launch {
            when (val result = postRepository.toggleFavorite(postId)) {
                is Result.Success -> {
                    // Refresh both lists after toggling favorite
                    loadPosts()
                    loadFavoritePosts()
                }
                is Result.Error -> {
                    // Handle error - you might want to show a snackbar or toast
                    _postsState.value = Result.Error(result.error)
                }
                else -> {}
            }
        }
    }

    fun searchPosts(query: String) {
        viewModelScope.launch {
            _searchState.value = Result.Loading

            when (val result = postRepository.searchPosts(query)) {
                is Result.Success -> {
                    _searchState.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _searchState.value = Result.Error(result.error)
                }
                else -> {}
            }
        }
    }

    fun clearSearch() {
        _searchState.value = Result.Idle
    }

    fun refreshAllData() {
        loadPosts()
        loadFavoritePosts()
        clearSearch()
    }

    // Helper function to get error message if needed
    fun getErrorMessage(result: Result<*>): String? {
        return if (result is Result.Error) {
            result.error.userMessage
        } else {
            null
        }
    }

    // Check if we have data available
    fun hasPosts(): Boolean {
        return postsState.value is Result.Success &&
                (postsState.value as Result.Success).data.isNotEmpty()
    }

    fun hasFavorites(): Boolean {
        return favoritePostsState.value is Result.Success &&
                (favoritePostsState.value as Result.Success).data.isNotEmpty()
    }
}