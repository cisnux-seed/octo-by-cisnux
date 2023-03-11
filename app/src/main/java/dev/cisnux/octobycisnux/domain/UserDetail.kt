package dev.cisnux.octobycisnux.domain

data class UserDetail(
    val username: String,
    val name: String?,
    val followers: Int,
    val following: Int,
    val profilePict: String,
    val location: String?
)