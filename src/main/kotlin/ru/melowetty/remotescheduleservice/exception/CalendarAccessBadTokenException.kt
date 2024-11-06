package ru.melowetty.remotescheduleservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class CalendarAccessBadTokenException(
    override val message: String
) : RuntimeException(message)
