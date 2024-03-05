package edu.oregonstate.cs492.githubsearchwithroom.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.githubsearchwithroom.data.GitHubRepo
import edu.oregonstate.cs492.githubsearchwithroom.data.GitHubReposRepository
import edu.oregonstate.cs492.githubsearchwithroom.data.GitHubService
import edu.oregonstate.cs492.githubsearchwithroom.util.LoadingStatus
import kotlinx.coroutines.launch

class GitHubSearchViewModel : ViewModel() {
    private val repository = GitHubReposRepository(GitHubService.create())

    private val _searchResults = MutableLiveData<List<GitHubRepo>?>(null)
    val searchResults: LiveData<List<GitHubRepo>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadSearchResults(query: String, sort: String?) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadRepositoriesSearch(query, sort)
            _searchResults.value = result.getOrNull()
            _error.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}