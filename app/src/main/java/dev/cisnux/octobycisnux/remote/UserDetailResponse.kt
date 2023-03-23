package dev.cisnux.octobycisnux.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.octobycisnux.domain.UserDetail

@JsonClass(generateAdapter = true)
data class UserDetailResponse(
    @Json(name = "id")
    val id: Int,

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
    id = id,
    username = login,
    name = name,
    totalFollowers = followers,
    totalFollowing = following,
    profilePict = avatarUrl,
    location = location,
)