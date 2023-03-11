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
    @Json(name = "login")
    val login: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "id")
    val id: Int,
)

fun List<UserResponse>.asUsers(): List<User> = map {
    User(
        id = it.id,
        username = it.login,
        profilePict = it.avatarUrl
    )
}