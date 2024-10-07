package ru.melowetty.remotescheduleservice.repository

import java.time.LocalDate
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import ru.melowetty.remotescheduleservice.repository.response.ExternalSchedule
import ru.melowetty.remotescheduleservice.repository.response.ExternalScheduleInfo
import ru.melowetty.remotescheduleservice.repository.response.Response

@FeignClient("schedule-service", url="\${api.schedule-service.url:}")
interface ScheduleRepository {
    @GetMapping("v3/schedule/{telegramId}")
    fun getUserSchedule(@PathVariable("telegramId") telegramId: Long,
                         @RequestParam("start") @DateTimeFormat(pattern = "dd.MM.yyyy") start: LocalDate,
                         @RequestParam("end") @DateTimeFormat(pattern = "dd.MM.yyyy") end: LocalDate,
    ): Response<ExternalSchedule>

    @GetMapping("v3/schedules")
    fun getAvailableSchedules(): Response<List<ExternalScheduleInfo>>
}