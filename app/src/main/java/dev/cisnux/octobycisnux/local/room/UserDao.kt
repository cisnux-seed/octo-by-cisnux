package dev.cisnux.octobycisnux.local.room

import androidx.room.*
import dev.cisnux.octobycisnux.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE is_favorite = 1")
    fun getFavoriteUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE user SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateUser(id: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS (SELECT * FROM user WHERE id = :id AND is_favorite = 1)")
    fun isFavoriteUser(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS (SELECT * FROM user WHERE id = :id)")
    fun isUserExist(id: Int): Boolean
}