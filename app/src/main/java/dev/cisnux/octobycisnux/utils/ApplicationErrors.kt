package dev.cisnux.octobycisnux.utils

sealed class ApplicationErrors(val message: String? = null) {
    class IOError(
        message: String?,
    ) : ApplicationErrors(message)

    class AuthenticationError(
        message: String?,
    ) : ApplicationErrors(message)

    class NotFoundError(
        message: String?,
    ) : ApplicationErrors(message)

    class AuthorizationError(
        message: String?,
    ) : ApplicationErrors(message)

    class ServerError(
        message: String?,
    ) : ApplicationErrors(message)
}