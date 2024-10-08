package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import ru.melowetty.remotescheduleservice.utils.DateUtils

data class Schedule(
    val lessons: List<Lesson>,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val start: LocalDate,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val end: LocalDate,
    val scheduleType: ScheduleType,
)
