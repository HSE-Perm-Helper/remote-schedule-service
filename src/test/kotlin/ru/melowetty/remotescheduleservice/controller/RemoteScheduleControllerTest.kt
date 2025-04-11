package ru.melowetty.remotescheduleservice.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import ru.melowetty.remotescheduleservice.exception.CalendarAccessBadTokenException
import ru.melowetty.remotescheduleservice.service.RemoteScheduleService

@WebMvcTest(RemoteScheduleController::class)
class RemoteScheduleControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var scheduleService: RemoteScheduleService

    @Test
    fun `get token when it not exists`() {
        val telegramId = 123L
        val token = "123"
        Mockito.`when`(scheduleService.getRemoteScheduleAsText(token))
            .thenThrow(CalendarAccessBadTokenException("Неверный токен для этого аккаунта"))

        val params = LinkedMultiValueMap<String, String>()
        params["telegramId"] = listOf(telegramId.toString())
        params["token"] = listOf(token)

        val builder: RequestBuilder = MockMvcRequestBuilders
            .get("/remote-schedule")
            .params(params)
            .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isForbidden)
            .andExpect { result: MvcResult -> Assertions.assertTrue(result.resolvedException is CalendarAccessBadTokenException) }
            .andExpect { result: MvcResult ->
                Assertions.assertEquals(
                    "Неверный токен для этого аккаунта", result.resolvedException
                        ?.message
                )
            }
    }
}