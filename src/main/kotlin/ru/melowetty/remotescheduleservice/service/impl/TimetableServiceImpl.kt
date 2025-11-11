package ru.melowetty.remotescheduleservice.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.repository.TimetableRepository
import ru.melowetty.remotescheduleservice.service.TimetableService

@Service
class TimetableServiceImpl(
    private val timetableRepository: TimetableRepository
) : TimetableService {
    override fun getUserLessons(telegramId: Long): List<Lesson> {
        val availableTimetables = timetableRepository.getAvailableTimetables().filter {
            it.scheduleType != ScheduleType.QUARTER_SCHEDULE
        }

        val deferredTimetables = availableTimetables.map { timetableInfo ->
            CoroutineScope(Dispatchers.IO).async {
                timetableRepository.getTimetable(telegramId, timetableInfo.id)
            }
        }

        val timetables = runBlocking {
            deferredTimetables.awaitAll()
        }

        return timetables.map { it.lessons }.flatten()
    }
}