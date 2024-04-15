package ru.melowetty.remotescheduleservice.model

import java.time.LocalDate

data class Schedule(
    val weekNumber: Int?,
    val lessons: List<Lesson>,
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val scheduleType: ScheduleType,
)
