package ru.melowetty.remotescheduleservice.service

import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.entity.CalendarTokenEntity
import ru.melowetty.remotescheduleservice.exception.CalendarTokenNotFoundException
import ru.melowetty.remotescheduleservice.repository.CalendarTokenRepository

@Service
class CalendarTokenService(
    private val calendarTokenRepository: CalendarTokenRepository,
) {
    fun verifyToken(token: String): CalendarTokenEntity? {
        val token = calendarTokenRepository.findByToken(token)
        return token
    }

    fun createOrUpdateToken(telegramId: Long): String {
        val generatedToken = generateToken()
        val currentTokenEntity = calendarTokenRepository.findById(telegramId).orElseGet {
            CalendarTokenEntity(telegramId, generatedToken, null)
        }

        calendarTokenRepository.save(
            currentTokenEntity.copy(
                token = generatedToken
            )
        )

        return generatedToken
    }

    fun getToken(telegramId: Long): String {
        val tokenEntity = calendarTokenRepository.findById(telegramId)
            .orElseThrow { throw CalendarTokenNotFoundException("Ключ для этого пользователя не найден!") }

        return tokenEntity.token
    }

    fun generateToken(): String {
        return RandomStringUtils.randomAlphanumeric(64)
    }

    fun getCurrentTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun markTokenAsUsed(token: String) {
        val tokenEntity = calendarTokenRepository.findByToken(token)
            ?: return

        calendarTokenRepository.save(tokenEntity.copy(lastFetch = getCurrentTime()))
    }
}