package dev.cisnux.octobycisnux.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.octobycisnux.local.room.UserDao
import dev.cisnux.octobycisnux.local.room.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDaoModule {

    @Singleton
    @Provides
    fun provideUserDao(
        userDatabase: UserDatabase
    ): UserDao = userDatabase.userDao()
}