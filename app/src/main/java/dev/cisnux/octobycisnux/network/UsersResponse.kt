package dev.cisnux.octobycisnux.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.octobycisnux.domain.User

@JsonClass(generateAdapter = true)
data class UsersResponse(

    @Json(name = "items")
    val items: List<UserResponse>
)

data class UserResponse(

    @Json(name = "gists_url")
    val gistsUrl: String,

    @Json(name = "repos_url")
    val reposUrl: String,

    @Json(name = "following_url")
    val followingUrl: String,

    @Json(name = "starred_url")
    val starredUrl: String,

    @Json(name = "login")
    val login: String,

    @Json(name = "followers_url")
    val followersUrl: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "id")
    val id: Int,

    @Json(name = "gravatar_id")
    val gravatarId: String,

    @Json(name = "organizations_url")
    val organizationsUrl: String
)

fun List<UserResponse>.asUsers(): List<User> = map {
    User(
        id = it.id,
        username = it.login,
        profilePict = it.avatarUrl
    )
}