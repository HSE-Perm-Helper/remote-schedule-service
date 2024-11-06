package ru.melowetty.remotescheduleservice.service

import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.repository.CalendarTokenRepository

@Service
class CalendarTokenService(
    private val calendarTokenRepository: CalendarTokenRepository,
) {
    fun verifyToken(telegramId: Long, token: String): Boolean {
        return false
    }

    fun createToken(telegramId: Long, token: String): String {
        return ""
    }

    fun checkExistsToken(telegramId: Long, token: String): Boolean {
        return false
    }
}