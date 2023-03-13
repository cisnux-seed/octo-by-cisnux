package dev.cisnux.octobycisnux.utils

sealed interface ApplicationNetworkStatus {
    object Loading : ApplicationNetworkStatus
    class Success(val isEmpty: Boolean = false) : ApplicationNetworkStatus
    class Failed(val message: String?) : ApplicationNetworkStatus
}