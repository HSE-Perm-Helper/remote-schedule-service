package ru.melowetty.remotescheduleservice.repository

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.melowetty.remotescheduleservice.repository.response.ExternalTimetable
import ru.melowetty.remotescheduleservice.repository.response.ExternalScheduleInfo

@FeignClient(name = "schedule-service", url = "\${api.schedule-service.url}")
interface TimetableRepository {
    @GetMapping("v3/users/{telegramId}/timetables/{timetableId}")
    fun getTimetable(
        @PathVariable("telegramId") telegramId: Long,
        @PathVariable("timetableId") timetableId: String,
    ): ExternalTimetable

    @GetMapping("v3/users/{telegramId}/timetables")
    fun getAvailableTimetables(
        @PathVariable("telegramId") telegramId: Long,
    ): List<ExternalScheduleInfo>
}