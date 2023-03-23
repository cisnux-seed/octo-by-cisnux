package dev.cisnux.octobycisnux.utils

sealed interface ApplicationStates {
    object Loading : ApplicationStates
    class Failed(val error: ApplicationErrors) : ApplicationStates
    object Success : ApplicationStates
}