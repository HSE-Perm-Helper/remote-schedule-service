package ru.melowetty.remotescheduleservice.service.impl

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ParameterList
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.parameter.Value
import net.fortuna.ical4j.model.property.*
import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.service.RemoteScheduleService
import ru.melowetty.remotescheduleservice.service.ScheduleService
import ru.melowetty.remotescheduleservice.utils.ScheduleUtils
import java.time.Duration

@Service
class RemoteScheduleServiceImpl(
    private val scheduleService: ScheduleService,
): RemoteScheduleService {
    override fun getRemoteScheduleAsText(telegramId: Long): String {
        val calendar = Calendar().withDefaults().fluentTarget
        addMetaDataToCalendar(calendar)

        val schedules = scheduleService.getUserSchedules(telegramId)
        val mergedSchedules = ScheduleUtils.mergeSchedules(schedules)
        mergedSchedules.forEach { calendar.add(it.toVEvent()) }
        
        return calendar.toString()
    }

    private fun addMetaDataToCalendar(calendar: Calendar) {
        val name = "Расписание"
        addCalendarName(calendar, name)


        val description = "Расписание пар в НИУ ВШЭ - Пермь by HSE Perm Helper"
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
            .getTimeZone("Asia/Yekaterinburg")
            .vTimeZone
        calendar.add(vTimeZone)

        val color = Color()
        color.value = "0:71:187"
        calendar.add(color)
        calendar.add(XProperty("X-APPLE-CALENDAR-COLOR", "#0047BB"))

        calendar.add(RefreshInterval(ParameterList(listOf(Value.DURATION)), Duration.ofHours(1)))
    }
}