package dev.cisnux.octobycisnux.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.cisnux.octobycisnux.domain.UserDetail
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationErrors
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _user: MutableLiveData<UserDetail> = MutableLiveData<UserDetail>()
    val user: LiveData<UserDetail> = _user

    private val _applicationNetworkStatus = MutableLiveData<ApplicationNetworkStatus>()
    val applicationNetworkStatus = _applicationNetworkStatus

    private val repository = UserRepository()

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName.toString()
    }

    fun getUserByUsername(username: String) = viewModelScope.launch {
        val result = repository.getUserByUsername(username)
        result.fold(
            { error ->
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Failed(
                    when (error) {
                        is ApplicationErrors.IOError -> {
                            "check your connection"
                        }
                        is ApplicationErrors.NotFoundError -> {
                            "the username you're looking for could not be found"
                        }
                        else -> null
                    }
                )
                error.message?.let {
                    Log.e(TAG, it)
                }
            },
            { userDetail ->
                _user.value = userDetail
                _applicationNetworkStatus.value = ApplicationNetworkStatus.Success
            }
        )
    }
}