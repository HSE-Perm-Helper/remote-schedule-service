package ru.melowetty.remotescheduleservice.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.melowetty.remotescheduleservice.model.Schedule

@FeignClient("schedule-service", url="http://31.129.105.79:8089/api")
interface ScheduleService {
    @GetMapping("v2/schedule/{telegramId}")
    fun getUserSchedules(@PathVariable("telegramId") telegramId: Long): List<Schedule>
}