package ru.melowetty.remotescheduleservice.repository

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.melowetty.remotescheduleservice.config.InternalFeignClientConfiguration
import ru.melowetty.remotescheduleservice.repository.response.ExternalSchedule
import ru.melowetty.remotescheduleservice.repository.response.Response

@FeignClient("schedule-service", url="http://localhost:8080/api", configuration = [InternalFeignClientConfiguration::class])
interface ScheduleRepository {
    @GetMapping("v3/schedule/{telegramId}")
    fun getUserSchedules(@PathVariable("telegramId") telegramId: Long): Response<List<ExternalSchedule>>
}