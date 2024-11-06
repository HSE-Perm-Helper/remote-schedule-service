package ru.melowetty.remotescheduleservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.melowetty.remotescheduleservice.controller.response.CalendarTokenResponse
import ru.melowetty.remotescheduleservice.service.CalendarTokenService

@RestController
@RequestMapping("remote-schedule-management")
class RemoteScheduleManagementController(
    private val calendarTokenService: CalendarTokenService
) {
    @GetMapping
    fun getCalendarToken(@RequestParam telegramId: Long): CalendarTokenResponse {
        return CalendarTokenResponse(
            calendarTokenService.getToken(telegramId)
        )
    }

    @PostMapping
    fun createOrUpdateCalendarToken(@RequestParam telegramId: Long): CalendarTokenResponse {
        return CalendarTokenResponse(
            calendarTokenService.createOrUpdateToken(telegramId)
        )
    }
}