package dev.cisnux.octobycisnux.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationErrors
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _applicationNetworkStatus = MutableLiveData<ApplicationNetworkStatus>()
    val applicationNetworkStatus = _applicationNetworkStatus

    private val repository = UserRepository()

    init {
        getUsers()
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName.toString()
    }

    private fun getUsers() = viewModelScope.launch {
        _applicationNetworkStatus.value = ApplicationNetworkStatus.Loading
        val results = repository.getUsers()
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
            { users ->
                _users.value = users
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Success
            },
        )
    }

    fun getUsersByUsername(username: String) = viewModelScope.launch {
        _applicationNetworkStatus.value = ApplicationNetworkStatus.Loading
        val results = repository.getUsersByUsername(username)
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
            { users ->
                _users.value = users
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Success
            },
        )
    }
}