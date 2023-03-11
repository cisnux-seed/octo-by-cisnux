package dev.cisnux.octobycisnux.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.octobycisnux.domain.UserDetail

@JsonClass(generateAdapter = true)
data class UserDetailResponse(

    @Json(name = "hireable")
    val hireable: Boolean?,

    @Json(name = "twitter_username")
    val twitterUsername: String?,

    @Json(name = "bio")
    val bio: String?,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "login")
    val login: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "blog")
    val blog: String,

    @Json(name = "public_gists")
    val publicGists: Int,

    @Json(name = "followers")
    val followers: Int,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "updated_at")
    val updatedAt: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "following")
    val following: Int,

    @Json(name = "site_admin")
    val siteAdmin: Boolean,

    @Json(name = "name")
    val name: String?,

    @Json(name = "company")
    val company: String?,

    @Json(name = "location")
    val location: String?,

    @Json(name = "id")
    val id: Int,

    @Json(name = "email")
    val email: String?
)

fun UserDetailResponse.asUserDetail() = UserDetail(
    username = login,
    name = name ?: "",
    followers = followers,
    following = following,
    profilePict = avatarUrl,
    location = location,
)