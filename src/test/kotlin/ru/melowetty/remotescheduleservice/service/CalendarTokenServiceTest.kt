package ru.melowetty.remotescheduleservice.service

import java.util.Optional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import ru.melowetty.remotescheduleservice.entity.CalendarTokenEntity
import ru.melowetty.remotescheduleservice.exception.CalendarTokenNotFoundException
import ru.melowetty.remotescheduleservice.repository.CalendarTokenRepository

@ExtendWith(MockitoExtension::class)
class CalendarTokenServiceTest {
    @Spy
    @InjectMocks
    private lateinit var tokenService: CalendarTokenService

    @Mock
    private lateinit var tokenRepository: CalendarTokenRepository

    @Test
    fun `test create token`() {
        val telegramId = 123L

        Mockito.`when`(tokenRepository.findById(Mockito.eq(telegramId))).thenReturn(Optional.empty())

        doReturn("secret")
            .`when`(tokenService).generateToken()

        val actual = tokenService.createOrUpdateToken(telegramId)

        Mockito.verify(tokenRepository, times(1)).save(
            eq(
                CalendarTokenEntity(
                    telegramId, "secret", null
                )
            )
        )

        Assertions.assertEquals(actual, "secret")
    }

    @Test
    fun `test update token`() {
        val telegramId = 1234L
        val token = "secret"

        doReturn(
            Optional.of(
                CalendarTokenEntity(telegramId, token, null)
            )
        ).`when`(tokenRepository).findById(1234L)

        doReturn("secret2")
            .`when`(tokenService).generateToken()

        val actual = tokenService.createOrUpdateToken(telegramId)

        Mockito.verify(tokenRepository, times(1)).save(
            eq(
                CalendarTokenEntity(
                    telegramId, "secret2", null
                )
            )
        )

        Assertions.assertEquals(actual, "secret2")
    }

    @Test
    fun `test generate token`() {
        val first = tokenService.generateToken()
        val second = tokenService.generateToken()

        Assertions.assertNotEquals(first, second)
        Assertions.assertEquals(first.length, 64)
        Assertions.assertEquals(second.length, 64)
    }

    @Test
    fun `test verify token when token is not valid`() {
        val currentToken = "secret2"

        doReturn(null)
            .`when`(tokenRepository).findByToken(currentToken)

        val result = tokenService.verifyToken(currentToken)

        Assertions.assertNull(result)
    }

    @Test
    fun `test verify token when token is valid`() {
        val currentToken = "secret2"

        doReturn(CalendarTokenEntity(
            telegramId = 123,
            token = currentToken,
            lastFetch = null,
        ))
            .`when`(tokenRepository).findByToken(currentToken)

        val result = tokenService.verifyToken(currentToken)

        Assertions.assertNotNull(result)
    }

    @Test
    fun `test get token when it exist`() {
        val telegramId = 123L
        val token = "secret"

        Mockito.`when`(tokenRepository.findById(telegramId))
            .thenReturn(Optional.of(CalendarTokenEntity(telegramId, token, null)))

        val actual = tokenService.getToken(telegramId)

        Assertions.assertEquals(token, actual)
    }

    @Test
    fun `test get token when it not exist`() {
        val telegramId = 123L

        Mockito.`when`(tokenRepository.findById(telegramId))
            .thenReturn(Optional.empty())

        Assertions.assertThrows(CalendarTokenNotFoundException::class.java) {
            tokenService.getToken(telegramId)
        }
    }

}