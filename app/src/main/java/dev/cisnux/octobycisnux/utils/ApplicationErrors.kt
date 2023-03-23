package dev.cisnux.octobycisnux.utils

sealed class ApplicationErrors(val message: String) {
    class IOError(
        message: String = "check your connection",
    ) : ApplicationErrors(message)

    class NotFoundError(
        message: String = "the username you're looking for could not be found",
    ) : ApplicationErrors(message)

    class ServerError(
        message: String = "the server undergoing maintenance",
    ) : ApplicationErrors(message)

    class AuthenticationError(
        message: String = "your credentials are invalid",
    ) : ApplicationErrors(message)
}