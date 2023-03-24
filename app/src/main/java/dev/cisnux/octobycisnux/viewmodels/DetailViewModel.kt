package dev.cisnux.octobycisnux.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.octobycisnux.domain.UserDetail
import dev.cisnux.octobycisnux.repository.UserRepository
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> get() = _userDetail
    private val _userDetailStates = MutableLiveData<SingleEvent<ApplicationStates>>()
    val userDetailStates: LiveData<SingleEvent<ApplicationStates>> get() = _userDetailStates

    fun getUserDetailByUsername(username: String) = viewModelScope.launch {
        _userDetailStates.value = SingleEvent(ApplicationStates.Loading)
        val results = repository.getUserDetailByUsername(username)
        results.fold({ applicationErrors ->
            applicationErrors?.let {
                _userDetailStates.value = SingleEvent(ApplicationStates.Failed(it))
            }
        }, { userDetail ->
            _userDetailStates.value = SingleEvent(ApplicationStates.Success)
            _userDetail.value = userDetail
        })
    }

    fun isFavoriteUser(id: Int): LiveData<Boolean> = repository.isFavoriteUser(id).asLiveData()

    fun updateFavoriteUser(userDetail: UserDetail, isFavoriteUser: Boolean) =
        viewModelScope.launch {
            repository.updateFavoriteUser(userDetail, isFavoriteUser)
        }
}
