package ru.melowetty.remotescheduleservice.service.impl

import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.Schedule
import ru.melowetty.remotescheduleservice.repository.ScheduleRepository
import ru.melowetty.remotescheduleservice.service.ScheduleService
import ru.melowetty.remotescheduleservice.utils.ScheduleUtils

@Service
class ScheduleServiceImpl(
    private val scheduleRepository: ScheduleRepository
): ScheduleService {
    override fun getUserLessons(telegramId: Long): List<Lesson> {
        val externalSchedulesResponse = scheduleRepository.getUserSchedules(telegramId)
        val externalSchedules = externalSchedulesResponse.response
        val schedules = externalSchedules.map { Schedule(it.lessons, it.start, it.end, it.scheduleType) }

        val mergedSchedules = ScheduleUtils.mergeSchedules(schedules)
        return mergedSchedules
    }
}