package ru.melowetty.remotescheduleservice.extension

import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Url
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.LessonType
import ru.melowetty.remotescheduleservice.utils.DateUtils
import ru.melowetty.remotescheduleservice.utils.UuidUtils

class LessonExtensions {
    companion object {
        /**
         * Converts lesson object to VEvent for import in calendar
         *
         * @return converted lesson to VEvent object
         */
        fun Lesson.toVEvent(currentDateTime: LocalDateTime): VEvent {
            val startDateTime = getLocalDateTimeFromTimeAsString(time.date, time.startTime)
            val endDateTime = getLocalDateTimeFromTimeAsString(time.date, time.endTime)

            val event = VEvent(
                startDateTime, endDateTime,
                lessonType.toEventSubject(subject)
            )
            val descriptionLines: MutableList<String> = mutableListOf()

            if (subGroup != null) {
                descriptionLines.add("$subGroup подгруппа")
            }

            if (lecturer != null) {
                descriptionLines.add(lecturer)
            }

            if (isOnline) {
                event.add(Location("Онлайн"))
                if (!links.isNullOrEmpty()) {

                    event.add(Url(URI.create(links[0])))

                    if (links.size > 1) {
                        descriptionLines.add("Дополнительные ссылки на пару: ")
                        links.subList(1, links.size).forEach { descriptionLines.add(it) }
                    }
                }

            } else {
                if (places.isNullOrEmpty()) {
                    if (lessonType == LessonType.COMMON_MINOR) {
                        descriptionLines.add(
                            "Информацию о времени и ссылке на майнор узнайте " +
                                    "подробнее в HSE App X или в системе РУЗ"
                        )
                    }

                } else {
                    event.add(Location(places.joinToString(" · ") {
                        "${it.office}, ${it.building} корпус"
                    }
                    ))
                }
            }

            if (additionalInfo?.isNotEmpty() == true) {
                descriptionLines.add(
                    "\n" +
                            "Дополнительная информация: ${additionalInfo.joinToString("\n")}"
                )
            }


            descriptionLines.add(
                "\n" +
                        "Последнее обновление: ${currentDateTime.format(DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN))}"
            )

            val id = UuidUtils.generateUUIDbySeed(hashCode().toString())
            event.add(Uid(id.toString()))
            event.add(
                Description(
                    descriptionLines.joinToString("\n")
                )
            )
            return event
        }

        private fun getLocalDateTimeFromTimeAsString(date: LocalDate, time: String): LocalDateTime {
            val dividedTime = time.split(":").map { it.toInt() }
            val localTime = LocalTime.of(dividedTime[0], dividedTime[1])
            return LocalDateTime.of(date, localTime)
        }
    }
}