package dev.cisnux.octobycisnux.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.cisnux.octobycisnux.domain.User

private const val TABLE_NAME = "user"

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey val id: Int,
    val login: String,
    @ColumnInfo("avatar_url") val avatarUrl: String,
    @ColumnInfo("is_favorite") val isFavorite: Boolean
)

fun List<UserEntity>.asUsers() = map { (id, login, avatarUrl) ->
    User(
        id = id,
        username = login,
        profilePict = avatarUrl,
    )
}