package ru.melowetty.remotescheduleservice.repository.response

import com.fasterxml.jackson.annotation.JsonFormat
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.utils.DateUtils
import java.time.LocalDate

data class ExternalSchedule(
    val weekNumber: Int?,
    val lessons: List<Lesson>,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val weekStart: LocalDate,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val weekEnd: LocalDate,
    val scheduleType: ScheduleType,
)