package dev.cisnux.octobycisnux.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int
    ): List<UserResponse>

    @GET("/search/users")
    suspend fun getUsersByUsername(
        @Query("q") username: String,
        @Query("per_page") perPage: Int
    ): UsersResponse

    @GET("/users/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String,
    ): UserDetailResponse

    @GET("/users/{username}/followers")
    suspend fun getFollowersByUsername(
        @Path("username") username: String,
        @Query("per_page") perPage: Int
    ): List<UserResponse>

    @GET("/users/{username}/following")
    suspend fun getFollowingByUsername(
        @Path("username") username: String,
        @Query("per_page") perPage: Int
    ): List<UserResponse>
}