package dev.cisnux.octobycisnux.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.octobycisnux.domain.User

@JsonClass(generateAdapter = true)
data class UserResponses(
    @Json(name = "items")
    val items: List<UserResponse>
)

data class UserResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "login")
    val login: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,
)

fun List<UserResponse>.asUsers(): List<User> = map { (id, login, avatarUrl) ->
    User(
        id = id,
        username = login,
        profilePict = avatarUrl
    )
}