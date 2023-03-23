package dev.cisnux.octobycisnux.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val appPackageName = "dev.cisnux.octobycisnux.preferences"
private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    appPackageName
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> = applicationContext.preferencesDataStore
}