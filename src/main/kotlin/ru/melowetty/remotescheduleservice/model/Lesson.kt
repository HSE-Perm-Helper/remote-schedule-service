package ru.melowetty.remotescheduleservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.LocalTime
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.util.RandomUidGenerator
import ru.melowetty.remotescheduleservice.utils.DateUtils
import ru.melowetty.remotescheduleservice.utils.EmojiCodes

data class Lesson(
    val subject: String,
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    val time: LessonTime,
    val lecturer: String?,
    val places: List<LessonPlace>? = null,
    val links: List<String>? = null,
    val additionalInfo: List<String>? = null,
    val lessonType: LessonType,
    val parentScheduleType: ScheduleType,
    val isOnline: Boolean
) {

    /**
     * Converts lesson object to VEvent for import in calendar
     *
     * @return converted lesson to VEvent object
     */
    fun toVEvent(): VEvent {
        val startDateTime = getLocalDateTimeFromTimeAsString(time.startTime)
        val endDateTime = getLocalDateTimeFromTimeAsString(time.endTime)

        val additionalInfoContainingSymbol =
            if (additionalInfo?.isNotEmpty() == true) EmojiCodes.ATTENTION_SYMBOL else ""

        val distantSymbol = if (isOnline) EmojiCodes.DISTANT_LESSON_SYMBOL else ""
        val event = VEvent(
            startDateTime, endDateTime,
            "${additionalInfoContainingSymbol}${distantSymbol}" +
                    lessonType.toEventSubject(subject)
        )
        val descriptionLines: MutableList<String> = mutableListOf()
        if (lecturer != null) {
            descriptionLines.add("Преподаватель: $lecturer")
        }
        if (isOnline) {
            if (!links.isNullOrEmpty()) {
                descriptionLines.add("Ссылка на пару: ${links[0]}")
                if (links.size > 1) {
                    descriptionLines.add("Дополнительные ссылки на пару: ")
                    links.subList(1, links.size).forEach { descriptionLines.add(it) }
                }
            } else {
                descriptionLines.add("Место: онлайн")
            }
        } else {
            if (places.isNullOrEmpty()) {
                if (lessonType == LessonType.COMMON_MINOR) {
                    descriptionLines.add(
                        "Информацию о времени и ссылке на майнор узнайте " +
                                "подробнее в HSE App X или в системе РУЗ"
                    )
                } else {
                    descriptionLines.add("Место: не указано")
                }
            } else {
                if (places.size > 1) {
                    descriptionLines.add("Место:")
                    places.forEach { descriptionLines.add("${it.office} - ${it.building} корпус") }
                } else {
                    descriptionLines.add("Место: ${places.first().office} - ${places.first().building} корпус")
                }
            }
        }
        if (additionalInfo?.isNotEmpty() == true) {
            descriptionLines.add(
                "\n" +
                        "Дополнительная информация: ${additionalInfo.joinToString("\n")}"
            )
        }
        if (parentScheduleType == ScheduleType.QUARTER_SCHEDULE) {
            descriptionLines.add(
                "\n" +
                        "* - пара взята из расписания на модуль, фактическое расписание " +
                        "может отличаться от этого"
            )
        }
        event.add(Uid(RandomUidGenerator().generateUid().value))
        event.add(
            Description(
                descriptionLines.joinToString("\n")
            )
        )
        return event
    }

    private fun getLocalDateTimeFromTimeAsString(time: String): LocalDateTime {
        val dividedTime = time.split(":").map { it.toInt() }
        val localTime = LocalTime.of(dividedTime[0], dividedTime[1])
        return LocalDateTime.of(this.time.date, localTime)
    }
}
