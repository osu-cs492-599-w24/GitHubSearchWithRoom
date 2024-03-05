package edu.oregonstate.cs492.githubsearchwithroom.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubReposRepository(
    private val service: GitHubService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadRepositoriesSearch(query: String, sort: String?): Result<List<GitHubRepo>> =
        withContext(ioDispatcher) {
            try {
                val response = service.searchRepositories(query, sort)
                if (response.isSuccessful) {
                    Result.success(response.body()?.items ?: listOf())
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}