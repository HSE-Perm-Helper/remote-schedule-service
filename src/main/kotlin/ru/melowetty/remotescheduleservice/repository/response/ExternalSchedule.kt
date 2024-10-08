package ru.melowetty.remotescheduleservice.repository.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.utils.DateUtils

data class ExternalSchedule(
    val number: Int?,
    val lessons: List<Lesson>,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val start: LocalDate,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val end: LocalDate,
    val scheduleType: ScheduleType,
)