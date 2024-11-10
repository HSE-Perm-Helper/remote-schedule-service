package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.net.URI
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Url
import ru.melowetty.remotescheduleservice.utils.DateUtils
import ru.melowetty.remotescheduleservice.utils.EmojiCodes
import ru.melowetty.remotescheduleservice.utils.UuidUtils

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
