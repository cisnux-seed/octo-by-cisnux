package dev.cisnux.octobycisnux.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.cisnux.octobycisnux.domain.User
import androidx.lifecycle.viewModelScope
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationErrors
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import dev.cisnux.octobycisnux.utils.SingleEvent
import kotlinx.coroutines.launch

class FollowViewModel : ViewModel() {
    private val _userFollows = MutableLiveData<List<User>>()
    val userFollows: LiveData<List<User>> = _userFollows
    private val _applicationNetworkStatus = MutableLiveData<SingleEvent<ApplicationNetworkStatus>>()
    val applicationNetworkStatus: LiveData<SingleEvent<ApplicationNetworkStatus>> =
        _applicationNetworkStatus
    private val repository = UserRepository()

    fun getFollowersOrFollowingByUsername(position: Int, username: String) = viewModelScope.launch {
        _applicationNetworkStatus.value = SingleEvent(ApplicationNetworkStatus.Loading)
        val results = if (position == 0) repository.getFollowersByUsername(
            username
        ) else repository.getFollowingByUsername(username)
        results.fold({ error ->
            _applicationNetworkStatus.value = SingleEvent(
                ApplicationNetworkStatus.Failed(
                    when (error) {
                        is ApplicationErrors.IOError -> {
                            "check your connection"
                        }
                        else -> null
                    }
                )
            )
            error.message?.let {
                Log.e(TAG, it)
            }
        }, { followersOrFollowing ->
            _userFollows.value = followersOrFollowing
            _applicationNetworkStatus.value = SingleEvent(ApplicationNetworkStatus.Success(followersOrFollowing.isEmpty()))
        })
    }

    companion object {
        private val TAG = FollowViewModel::class.java.simpleName.toString()
    }
}