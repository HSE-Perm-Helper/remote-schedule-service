package ru.melowetty.remotescheduleservice.controller

import jakarta.ws.rs.core.MediaType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import ru.melowetty.remotescheduleservice.exception.CalendarTokenNotFoundException
import ru.melowetty.remotescheduleservice.service.CalendarTokenService

@WebMvcTest(RemoteScheduleManagementController::class)
class RemoteScheduleManagementControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenService: CalendarTokenService

    @Test
    fun `get token when it exists`() {
        val telegramId = 123L
        Mockito.`when`(tokenService.getToken(telegramId)).thenReturn("secret")

        val params = LinkedMultiValueMap<String, String>()
        params.put("telegramId", listOf(telegramId.toString()))

        val builder: RequestBuilder = MockMvcRequestBuilders
            .get("/remote-schedule-management")
            .params(params)
            .accept(MediaType.APPLICATION_JSON)

        val result = mockMvc.perform(builder).andReturn()

        val expected = "{token: \"secret\"}"
        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }

    @Test
    fun `get token when it not exists`() {
        val telegramId = 123L
        Mockito.`when`(tokenService.getToken(telegramId))
            .thenThrow(CalendarTokenNotFoundException("Такой токен не найден"))

        val params = LinkedMultiValueMap<String, String>()
        params["telegramId"] = listOf(telegramId.toString())

        val builder: RequestBuilder = MockMvcRequestBuilders
            .get("/remote-schedule-management")
            .params(params)
            .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isNotFound())
            .andExpect { result: MvcResult -> Assertions.assertTrue(result.resolvedException is CalendarTokenNotFoundException) }
            .andExpect { result: MvcResult ->
                Assertions.assertEquals(
                    "Такой токен не найден", result.resolvedException
                        ?.message
                )
            }
    }

    @Test
    fun `create or update token`() {
        val telegramId = 123L
        Mockito.`when`(tokenService.createOrUpdateToken(telegramId)).thenReturn("secret")

        val params = LinkedMultiValueMap<String, String>()
        params["telegramId"] = listOf(telegramId.toString())

        val builder: RequestBuilder = MockMvcRequestBuilders
            .post("/remote-schedule-management")
            .params(params)
            .accept(MediaType.APPLICATION_JSON)

        val result = mockMvc.perform(builder).andReturn()

        val expected = "{token: \"secret\"}"
        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }
}