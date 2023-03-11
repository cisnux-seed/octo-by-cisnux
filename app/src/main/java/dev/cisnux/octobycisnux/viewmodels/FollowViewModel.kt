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
import kotlinx.coroutines.launch

class FollowViewModel : ViewModel() {
    private val _userFollows = MutableLiveData<List<User>>()
    val userFollows: LiveData<List<User>> = _userFollows

    private val repository = UserRepository()

    private val _applicationNetworkStatus = MutableLiveData<ApplicationNetworkStatus>()
    val applicationNetworkStatus: LiveData<ApplicationNetworkStatus> = _applicationNetworkStatus

    companion object {
        private val TAG = FollowViewModel::class.java.simpleName.toString()
    }

    fun getFollowersOrFollowingByUsername(position: Int, username: String) = viewModelScope.launch {
        _applicationNetworkStatus.value = ApplicationNetworkStatus.Loading
        val results = if (position == 1) repository.getFollowersByUsername(
            username
        ) else repository.getFollowingByUsername(username)

        results.fold(
            { error ->
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Failed(
                    when (error) {
                        is ApplicationErrors.IOError -> {
                            "check your connection"
                        }
                        else -> null
                    }
                )
                error.message?.let {
                    Log.e(TAG, it)
                }
            },
            { followers ->
                _userFollows.value = followers
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Success
            },
        )
    }
}