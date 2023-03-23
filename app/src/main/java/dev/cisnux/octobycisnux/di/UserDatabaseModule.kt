package dev.cisnux.octobycisnux.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.cisnux.octobycisnux.local.room.UserDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserDatabaseModule {
    private const val DATABASE_NAME = "user.db"

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext applicationContext: Context
    ): UserDatabase {
        val userDatabaseBuilder = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            DATABASE_NAME
        )
        return userDatabaseBuilder.build()
    }
}