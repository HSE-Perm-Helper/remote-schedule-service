package ru.melowetty.remotescheduleservice.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import ru.melowetty.remotescheduleservice.exception.CalendarAccessBadTokenException
import ru.melowetty.remotescheduleservice.service.impl.RemoteScheduleServiceImpl

@ExtendWith(MockitoExtension::class)
class RemoteScheduleServiceTest {
    @InjectMocks
    private lateinit var remoteScheduleService: RemoteScheduleServiceImpl

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var tokenService: CalendarTokenService

    @Test
    fun `exception when token is not valid`() {
        val telegramId = 123L
        val token = "secret"
        Mockito.`when`(tokenService.verifyToken(token)).thenReturn(null)

        Assertions.assertThrows(CalendarAccessBadTokenException::class.java) {
            remoteScheduleService.getRemoteScheduleAsText(token)
        }
    }
}