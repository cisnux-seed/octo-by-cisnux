package dev.cisnux.octobycisnux.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
internal class FavoriteUserViewModel @Inject constructor(repository: UserRepository) : ViewModel() {
    val favoriteUsers: LiveData<List<User>> = repository.favoriteUsers.asLiveData()
}