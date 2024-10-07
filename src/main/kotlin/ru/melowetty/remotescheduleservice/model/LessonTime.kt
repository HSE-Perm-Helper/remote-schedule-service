package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import ru.melowetty.remotescheduleservice.utils.DateUtils

data class LessonTime(
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
)
