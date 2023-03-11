package dev.cisnux.octobycisnux.utils

sealed interface ApplicationNetworkStatus {
    object Loading : ApplicationNetworkStatus
    object Success : ApplicationNetworkStatus
    class Failed(val message: String?) : ApplicationNetworkStatus
}