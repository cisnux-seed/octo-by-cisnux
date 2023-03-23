package dev.cisnux.octobycisnux.viewmodels

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _users = MediatorLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
    private val _usersStates = MutableLiveData<SingleEvent<ApplicationStates>>()
    val usersStates: LiveData<SingleEvent<ApplicationStates>> get() = _usersStates
    val hasFocus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        refreshLocalUsers()
        _users.addSource(repository.users.asLiveData()) {
            _users.value = it
        }
    }

    fun getUsersByUsername(username: String) = viewModelScope.launch {
        _usersStates.value = SingleEvent(ApplicationStates.Loading)
        val results = repository.getUsersByUsername(username)
        results.fold({ applicationErrors ->
            _usersStates.value =
                applicationErrors?.let { SingleEvent(ApplicationStates.Failed(it)) }
        }, { users ->
            _users.value = users
            _usersStates.value = SingleEvent(ApplicationStates.Success)
        })
    }

    private fun refreshLocalUsers() = viewModelScope.launch {
        Log.d(HomeViewModel::class.java.simpleName, "refreshLocalUsers")
        _usersStates.value = SingleEvent(ApplicationStates.Loading)
        val results = repository.refreshLocalUsers()
        _usersStates.value = SingleEvent(
            results?.let { ApplicationStates.Failed(it) }
                ?: ApplicationStates.Success)
    }
}