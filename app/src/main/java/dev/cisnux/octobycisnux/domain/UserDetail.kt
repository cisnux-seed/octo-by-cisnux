package dev.cisnux.octobycisnux.domain

data class UserDetail(
    val id: Int,
    val username: String,
    val name: String?,
    val totalFollowers: Int,
    val totalFollowing: Int,
    val profilePict: String,
    val location: String?,
)