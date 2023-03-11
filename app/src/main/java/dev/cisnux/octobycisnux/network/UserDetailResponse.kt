package dev.cisnux.octobycisnux.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.octobycisnux.domain.UserDetail

@JsonClass(generateAdapter = true)
data class UserDetailResponse(
    @Json(name = "login")
    val login: String,

    @Json(name = "followers")
    val followers: Int,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "following")
    val following: Int,

    @Json(name = "name")
    val name: String?,

    @Json(name = "location")
    val location: String?,
)

fun UserDetailResponse.asUserDetail() = UserDetail(
    username = login,
    name = name,
    followers = followers,
    following = following,
    profilePict = avatarUrl,
    location = location,
)