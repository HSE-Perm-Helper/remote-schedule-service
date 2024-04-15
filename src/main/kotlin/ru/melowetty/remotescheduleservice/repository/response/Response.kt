package ru.melowetty.remotescheduleservice.repository.response

data class Response<T>(
    val error: Boolean,
    val response: T
) {
}