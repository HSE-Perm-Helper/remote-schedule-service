package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import ru.melowetty.remotescheduleservice.utils.DateUtils

data class Lesson(
    val subject: String,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val time: LessonTime,
    val lecturer: String?,
    val subGroup: Int? = null,
    val places: List<LessonPlace>? = null,
    val links: List<String>? = null,
    val additionalInfo: List<String>? = null,
    val lessonType: LessonType,
    val parentScheduleType: ScheduleType,
    val isOnline: Boolean
)
