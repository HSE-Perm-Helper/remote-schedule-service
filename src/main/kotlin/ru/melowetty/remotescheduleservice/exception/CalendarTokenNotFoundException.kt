package ru.melowetty.remotescheduleservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class CalendarTokenNotFoundException(
    override val message: String
) : RuntimeException(message)