package edu.oregonstate.cs492.githubsearchwithroom.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.githubsearchwithroom.data.AppDatabase
import edu.oregonstate.cs492.githubsearchwithroom.data.BookmarkedReposRepository
import edu.oregonstate.cs492.githubsearchwithroom.data.GitHubRepo
import kotlinx.coroutines.launch

class BookmarkedReposViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkedReposRepository(
        AppDatabase.getInstance(application).gitHubRepoDao()
    )

    val bookmarkedRepos = repository.getAllBookmarkedRepos().asLiveData()

    fun addBookmarkedRepo(repo: GitHubRepo) {
        viewModelScope.launch {
            repository.insertBookmarkedRepo(repo)
        }
    }

    fun removeBookmarkedRepo(repo: GitHubRepo) {
        viewModelScope.launch {
            repository.deleteBookmarkedRepo(repo)
        }
    }

    fun getBookmarkedRepoByName(name: String) =
        repository.getBookmarkedRepoByName(name).asLiveData()
}