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

    private val _postsState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val postsState: StateFlow<Result<List<Post>>> = _postsState.asStateFlow()

    private val _favoritePostsState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val favoritePostsState: StateFlow<Result<List<Post>>> = _favoritePostsState.asStateFlow()

    private val _searchState = MutableStateFlow<Result<List<Post>>>(Result.Idle)
    val searchState: StateFlow<Result<List<Post>>> = _searchState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private val pageSize = 20
    private var canLoadMore = true

    init {
        loadPosts()
        loadFavoritePosts()
    }

    fun loadPosts(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
                currentPage = 1
                canLoadMore = true
            } else {
                _isLoading.value = true
            }

            _postsState.value = Result.Loading

            when (val result = postRepository.getPosts(page = currentPage, pageSize = pageSize)) {
                is Result.Success -> {
                    _postsState.value = Result.Success(result.data)
                    canLoadMore = result.data.size >= pageSize
                }
                is Result.Error -> {
                    _postsState.value = Result.Error(result.error)
                }
                else -> {}
            }

            _isLoading.value = false
            _isRefreshing.value = false
        }
    }

    fun loadMorePosts() {
        if (!canLoadMore || _isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentPage++

            when (val result = postRepository.getPosts(page = currentPage, pageSize = pageSize)) {
                is Result.Success -> {
                    val currentPosts = (_postsState.value as? Result.Success)?.data ?: emptyList()
                    val newPosts = currentPosts + result.data
                    _postsState.value = Result.Success(newPosts)
                    canLoadMore = result.data.size >= pageSize
                }
                is Result.Error -> {
                    // Revert page increment on error
                    currentPage--
                    _postsState.value = Result.Error(result.error)
                }
                else -> {}
            }

            _isLoadingMore.value = false
        }
    }

    fun refreshData() {
        loadPosts(isRefresh = true)
        loadFavoritePosts()
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
                    loadPosts()
                    loadFavoritePosts()
                }
                is Result.Error -> {
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
        refreshData()
        clearSearch()
    }

    fun getErrorMessage(result: Result<*>): String? {
        return if (result is Result.Error) {
            result.error.userMessage
        } else {
            null
        }
    }

    fun hasPosts(): Boolean {
        return postsState.value is Result.Success &&
                (postsState.value as Result.Success).data.isNotEmpty()
    }

    fun hasFavorites(): Boolean {
        return favoritePostsState.value is Result.Success &&
                (favoritePostsState.value as Result.Success).data.isNotEmpty()
    }
}