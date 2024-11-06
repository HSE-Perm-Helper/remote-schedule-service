package ru.melowetty.remotescheduleservice.service

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.entity.CalendarTokenEntity
import ru.melowetty.remotescheduleservice.exception.CalendarTokenNotFoundException
import ru.melowetty.remotescheduleservice.repository.CalendarTokenRepository

@Service
class CalendarTokenService(
    private val calendarTokenRepository: CalendarTokenRepository,
) {
    fun verifyToken(telegramId: Long, token: String): Boolean {
        val realToken = getToken(telegramId)
        return realToken == token
    }

    fun createOrUpdateToken(telegramId: Long): String {
        val generatedToken = generateToken()
        val currentTokenEntity = calendarTokenRepository.findById(telegramId).orElseGet {
            CalendarTokenEntity(telegramId, generatedToken)
        }

        calendarTokenRepository.save(currentTokenEntity.copy(
            token = generatedToken
        ))

        return generatedToken
    }

    fun getToken(telegramId: Long): String {
        val tokenEntity = calendarTokenRepository.findById(telegramId)
            .orElseThrow {  throw CalendarTokenNotFoundException("Ключ для этого пользователя не найден!") }

        return tokenEntity.token
    }

    fun generateToken(): String {
        return RandomStringUtils.randomAlphanumeric(64)
    }
}