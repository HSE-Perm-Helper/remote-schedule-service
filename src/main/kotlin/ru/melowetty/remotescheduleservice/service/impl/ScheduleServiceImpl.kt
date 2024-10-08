package ru.melowetty.remotescheduleservice.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.repository.ScheduleRepository
import ru.melowetty.remotescheduleservice.service.ScheduleService

@Service
class ScheduleServiceImpl(
    private val scheduleRepository: ScheduleRepository
) : ScheduleService {
    override fun getUserLessons(telegramId: Long): List<Lesson> {
        val availableSchedules = scheduleRepository.getAvailableSchedules().response.filter {
            it.scheduleType != ScheduleType.QUARTER_SCHEDULE
        }

        val deferredSchedules = availableSchedules.map { scheduleInfo ->
            CoroutineScope(Dispatchers.IO).async {
                scheduleRepository.getUserSchedule(telegramId, scheduleInfo.start, scheduleInfo.end).response
            }
        }

        val schedules = runBlocking {
            deferredSchedules.awaitAll()
        }

        return schedules.map { it.lessons }.flatten()
    }
}