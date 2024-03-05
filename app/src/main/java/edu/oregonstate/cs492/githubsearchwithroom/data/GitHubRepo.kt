package edu.oregonstate.cs492.githubsearchwithroom.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    @Json(name = "full_name") val name: String,
    val description: String?,
    @Json(name = "html_url") val url: String,
    @Json(name = "stargazers_count") val stars: Int
) : Serializable