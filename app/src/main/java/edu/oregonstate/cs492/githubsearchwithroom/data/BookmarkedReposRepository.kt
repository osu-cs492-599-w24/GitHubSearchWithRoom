package edu.oregonstate.cs492.githubsearchwithroom.data

import androidx.lifecycle.asLiveData

class BookmarkedReposRepository(
    private val dao: GitHubRepoDao
) {
    suspend fun insertBookmarkedRepo(repo: GitHubRepo) = dao.insert(repo)
    suspend fun deleteBookmarkedRepo(repo: GitHubRepo) = dao.delete(repo)
    fun getAllBookmarkedRepos() = dao.getAllRepos()
    fun getBookmarkedRepoByName(name: String) = dao.getRepoByName(name)
}