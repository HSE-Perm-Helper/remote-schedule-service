package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import ru.melowetty.remotescheduleservice.utils.DateUtils
import java.time.LocalDate

data class Schedule(
    val lessons: List<Lesson>,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val weekStart: LocalDate,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val weekEnd: LocalDate,
    val scheduleType: ScheduleType,
)
