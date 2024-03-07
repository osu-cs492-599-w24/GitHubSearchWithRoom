package edu.oregonstate.cs492.githubsearchwithroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GitHubRepoDao {
    @Insert
    suspend fun insert(repo: GitHubRepo)

    @Delete
    suspend fun delete(repo: GitHubRepo)

    @Query("SELECT * FROM GitHubRepo")
    fun getAllRepos() : Flow<List<GitHubRepo>>

    @Query("SELECT * FROM GitHubRepo WHERE name = :name LIMIT 1")
    fun getRepoByName(name: String) : Flow<GitHubRepo?>
}