package ru.melowetty.remotescheduleservice.service.impl

import java.time.Duration
import java.time.LocalDateTime
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ParameterList
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.parameter.Value
import net.fortuna.ical4j.model.property.Color
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.Method
import net.fortuna.ical4j.model.property.Name
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.RefreshInterval
import net.fortuna.ical4j.model.property.XProperty
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.exception.CalendarAccessBadTokenException
import ru.melowetty.remotescheduleservice.extension.LessonExtensions.Companion.toVEvent
import ru.melowetty.remotescheduleservice.service.CalendarTokenService
import ru.melowetty.remotescheduleservice.service.RemoteScheduleService
import ru.melowetty.remotescheduleservice.service.ScheduleService
import ru.melowetty.remotescheduleservice.utils.DateUtils

@Service
class RemoteScheduleServiceImpl(
    private val scheduleService: ScheduleService,
    private val tokenService: CalendarTokenService
) : RemoteScheduleService {
    override fun getRemoteScheduleAsText(token: String): String {
        val token = tokenService.verifyToken(token)
            ?: throw CalendarAccessBadTokenException("Недостаточно прав для просмотра этого календаря")

        val calendar = Calendar().withDefaults().fluentTarget
        addMetaDataToCalendar(calendar)

        val currentDateTime = LocalDateTime.now(DateUtils.timeZone)
        val lessons = scheduleService.getUserLessons(token.telegramId)
        lessons.forEach { calendar.add(it.toVEvent(currentDateTime)) }

        tokenService.markTokenAsUsed(token.token)

        return calendar.toString()
    }

    private fun addMetaDataToCalendar(calendar: Calendar) {
        val name = "Расписание"
        addCalendarName(calendar, name)


        val description = "Расписание пар"
        addCalendarDescription(calendar, description)

        addAdditionalCalendarData(calendar)
    }

    private fun addCalendarName(calendar: Calendar, name: String) {
        calendar.add(Name(name))
        calendar.add(XProperty("X-WR-CALNAME", name))
    }

    private fun addCalendarDescription(calendar: Calendar, description: String) {
        calendar.add(Description(description))
        calendar.add(XProperty("X-WR-CALDESC", description))
    }

    private fun addAdditionalCalendarData(calendar: Calendar) {
        calendar.add(ProdId("-//HSE Perm Helper//Расписание пар 1.0//RU"))
        calendar.add(Method(Method.VALUE_PUBLISH))

        val vTimeZone = TimeZoneRegistryFactory
            .getInstance()
            .createRegistry()
            .getTimeZone(DateUtils.TIME_ZONE_STR)
            .vTimeZone

        calendar.add(vTimeZone)

        calendar.add(XProperty("X-WR-TIMEZONE", DateUtils.TIME_ZONE_STR))

        val color = Color()
        color.value = "0:71:187"
        calendar.add(color)
        calendar.add(XProperty("X-APPLE-CALENDAR-COLOR", "#0047BB"))

        calendar.add(RefreshInterval(ParameterList(listOf(Value.DURATION)), Duration.ofHours(1)))
        calendar.add(XProperty("X-PUBLISHED-TTL", Duration.ofHours(1).toString()))
    }
}