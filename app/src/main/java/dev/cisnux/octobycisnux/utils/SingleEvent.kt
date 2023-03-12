package dev.cisnux.octobycisnux.utils

// The wrapper class
open class SingleEvent<out T>(val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }
}