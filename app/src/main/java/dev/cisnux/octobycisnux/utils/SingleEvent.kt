package dev.cisnux.octobycisnux.utils

// The wrapper class
open class SingleEvent<out T>(val content: T) {
    private var _hasBeenHandled = false

    fun getContentIfNotHandled(): T? = if (_hasBeenHandled) {
        null
    } else {
        _hasBeenHandled = true
        content
    }
}