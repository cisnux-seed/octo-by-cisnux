package dev.cisnux.octobycisnux.repository

import arrow.core.Either
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.domain.UserDetail
import dev.cisnux.octobycisnux.local.datastore.SettingPreferences
import dev.cisnux.octobycisnux.local.entity.UserEntity
import dev.cisnux.octobycisnux.local.entity.asUsers
import dev.cisnux.octobycisnux.local.room.UserDao
import dev.cisnux.octobycisnux.remote.UserService
import dev.cisnux.octobycisnux.remote.asUserDetail
import dev.cisnux.octobycisnux.remote.asUsers
import dev.cisnux.octobycisnux.utils.ApplicationErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val settingPreferences: SettingPreferences,
    private val userDao: UserDao
) {
    val themeSetting get() = settingPreferences.getThemeSetting()
    val users get() = userDao.getUsers().map(List<UserEntity>::asUsers)
    val favoriteUsers get() = userDao.getFavoriteUsers().map(List<UserEntity>::asUsers)

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        settingPreferences.saveThemeSetting(isDarkModeActive)

    fun isFavoriteUser(id: Int) = userDao.isFavoriteUser(id)

    suspend fun updateFavoriteUser(userDetail: UserDetail, isFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            val isUserExist = userDao.isUserExist(userDetail.id)
            if (!isUserExist)
                userDao.insertUser(
                    UserEntity(
                        id = userDetail.id,
                        login = userDetail.username,
                        avatarUrl = userDetail.profilePict,
                        isFavorite = isFavorite,
                    )
                )
            else userDao.updateUser(userDetail.id, isFavorite)
        }


    suspend fun refreshLocalUsers(perPage: Int = 20): ApplicationErrors? =
        withContext(Dispatchers.IO) {
            try {
                val userResponses = userService.getUsers(perPage)
                val userEntities = userResponses.map { (id, login, avatarUrl) ->
                    val isFavoriteUser = userDao.isFavoriteUser(id).first()
                    UserEntity(
                        id,
                        login,
                        avatarUrl,
                        isFavoriteUser
                    )
                }
                userDao.insertUsers(userEntities)
                null
            } catch (error: IOException) {
                ApplicationErrors.IOError()
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = NETWORK_ERROR[response?.code()]
                httpError
            }
        }

    suspend fun getUserDetailByUsername(username: String): Either<ApplicationErrors?, UserDetail> =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    userService.getUserByUsername(username).asUserDetail()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError())
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = NETWORK_ERROR[response?.code()]
                Either.Left(httpError)
            }
        }

    suspend fun getUsersByUsername(
        username: String,
        perPage: Int = 20
    ): Either<ApplicationErrors?, List<User>> =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    userService
                        .getUsersByUsername(username, perPage)
                        .items
                        .asUsers()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError())
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = NETWORK_ERROR[response?.code()]
                Either.Left(httpError)
            }
        }

    suspend fun getFollowersByUsername(
        username: String,
        perPage: Int = 20
    ): Either<ApplicationErrors?, List<User>> =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    userService.getFollowersByUsername(username, perPage).asUsers()
                )
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError())
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = NETWORK_ERROR[response?.code()]
                Either.Left(httpError)
            }
        }

    suspend fun getFollowingByUsername(
        username: String,
        perPage: Int = 20
    ): Either<ApplicationErrors?, List<User>> =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(userService.getFollowingByUsername(username, perPage).asUsers())
            } catch (error: IOException) {
                Either.Left(ApplicationErrors.IOError())
            } catch (error: HttpException) {
                val response = error.response()
                val httpError = NETWORK_ERROR[response?.code()]
                Either.Left(httpError)
            }
        }

    companion object {
        private val NETWORK_ERROR = mapOf(
            401 to ApplicationErrors.AuthenticationError(),
            404 to ApplicationErrors.NotFoundError(),
            500 to ApplicationErrors.ServerError(),
        )
    }
}