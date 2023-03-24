package dev.cisnux.octobycisnux.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FollowersFollowingViewModel @Inject constructor(private val repository: UserRepository) :
    ViewModel() {
    private val _followersFollowing = MutableLiveData<List<User>>()
    val followersFollowing: LiveData<List<User>> get() = _followersFollowing
    private val _followersFollowingStates = MutableLiveData<SingleEvent<ApplicationStates>>()
    val followersFollowingStates: LiveData<SingleEvent<ApplicationStates>> get() = _followersFollowingStates

    fun getFollowersByUsername(username: String) = viewModelScope.launch {
        _followersFollowingStates.value = SingleEvent(ApplicationStates.Loading)
        val results = repository.getFollowersByUsername(username)
        results.fold({ applicationErrors ->
            applicationErrors?.let {
                _followersFollowingStates.value = SingleEvent(ApplicationStates.Failed(it))
            }
        }, { users ->
            _followersFollowing.value = users
            _followersFollowingStates.value = SingleEvent(ApplicationStates.Success)
        })
    }

    fun getFollowingByUsername(username: String) = viewModelScope.launch {
        _followersFollowingStates.value = SingleEvent(ApplicationStates.Loading)
        val results = repository.getFollowingByUsername(username)
        results.fold({ applicationErrors ->
            applicationErrors?.let {
                _followersFollowingStates.value = SingleEvent(ApplicationStates.Failed(it))
            }
        }, { users ->
            _followersFollowing.value = users
            _followersFollowingStates.value = SingleEvent(ApplicationStates.Success)
        })
    }
}