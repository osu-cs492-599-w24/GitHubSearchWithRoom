package edu.oregonstate.cs492.githubsearchwithroom.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String? = "stars"
    ): Response<GitHubSearchResults>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GitHubService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(GitHubService::class.java)
        }
    }
}