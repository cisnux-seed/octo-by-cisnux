package dev.cisnux.octobycisnux.repository

import arrow.core.Either
import dev.cisnux.octobycisnux.network.UserNetwork
import dev.cisnux.octobycisnux.network.asUserDetail
import dev.cisnux.octobycisnux.network.asUsers
import dev.cisnux.octobycisnux.utils.ApplicationErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository {
    suspend fun getUsers(perPage: Int = 20) = withContext(Dispatchers.IO) {
        try {
            Either.Right(UserNetwork.usersService.getUsers(perPage).asUsers())
        } catch (error: IOException) {
            Either.Left(ApplicationErrors.IOError(error.message))
        } catch (error: HttpException) {
            val response = error.response()
            val httpError = when (response?.code()) {
                401 -> ApplicationErrors.AuthenticationError(
                    message = error.message()
                )
                403 -> ApplicationErrors.AuthorizationError(
                    message = error.message(),
                )
                404 -> ApplicationErrors.AuthenticationError(
                    message = error.message(),
                )
                else -> ApplicationErrors.ServerError(
                    message = error.message(),
                )
            }
            Either.Left(httpError)
        }
    }

    suspend fun getUserByUsername(username: String) = withContext(Dispatchers.IO) {
        try {
            Either.Right(UserNetwork.usersService.getUserByUsername(username).asUserDetail())
        } catch (error: IOException) {
            Either.Left(ApplicationErrors.IOError(error.message))
        } catch (error: HttpException) {
            val response = error.response()
            val httpError = when (response?.code()) {
                401 -> ApplicationErrors.AuthenticationError(
                    message = error.message()
                )
                403 -> ApplicationErrors.AuthorizationError(
                    message = error.message(),
                )
                404 -> ApplicationErrors.AuthenticationError(
                    message = error.message(),
                )
                else -> ApplicationErrors.ServerError(
                    message = error.message(),
                )
            }
            Either.Left(httpError)
        }
    }

    suspend fun getFollowersByUsername(username: String, perPage: Int = 20) =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    UserNetwork
                        .usersService
                        .getFollowersByUsername(username, perPage)
                        .asUsers()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError(error.message))
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = when (response?.code()) {
                    401 -> ApplicationErrors.AuthenticationError(
                        message = error.message()
                    )
                    403 -> ApplicationErrors.AuthorizationError(
                        message = error.message(),
                    )
                    404 -> ApplicationErrors.AuthenticationError(
                        message = error.message(),
                    )
                    else -> ApplicationErrors.ServerError(
                        message = error.message(),
                    )
                }
                Either.Left(httpError)
            }
        }

    suspend fun getFollowingByUsername(username: String, perPage: Int = 20) =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    UserNetwork
                        .usersService
                        .getFollowingByUsername(username, perPage)
                        .asUsers()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError(error.message))
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = when (response?.code()) {
                    401 -> ApplicationErrors.AuthenticationError(
                        message = error.message()
                    )
                    403 -> ApplicationErrors.AuthorizationError(
                        message = error.message()
                    )
                    404 -> ApplicationErrors.AuthenticationError(
                        message = error.message()
                    )
                    else -> ApplicationErrors.ServerError(
                        message = error.message()
                    )
                }
                Either.Left(httpError)
            }
        }

    suspend fun getUsersByUsername(username: String, perPage: Int = 20) =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    UserNetwork
                        .usersService
                        .getUsersByUsername(username, perPage)
                        .items
                        .asUsers()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError(error.message))
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = when (response?.code()) {
                    401 -> ApplicationErrors.AuthenticationError(
                        message = error.message()
                    )
                    403 -> ApplicationErrors.AuthorizationError(
                        message = error.message()
                    )
                    404 -> ApplicationErrors.AuthenticationError(
                        message = error.message()
                    )
                    else -> ApplicationErrors.ServerError(
                        message = error.message()
                    )
                }
                Either.Left(httpError)
            }
        }
}