package dev.cisnux.octobycisnux.utils

sealed interface ApplicationNetworkStatus {
    object Loading : ApplicationNetworkStatus
    class Success() : ApplicationNetworkStatus{
        var isEmpty: Boolean = false
            private set

        constructor(isEmpty: Boolean):this(){
            this.isEmpty = isEmpty
        }
    }
    class Failed(val message: String?) : ApplicationNetworkStatus
}