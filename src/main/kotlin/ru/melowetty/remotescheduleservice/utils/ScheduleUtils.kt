package ru.melowetty.remotescheduleservice.utils

import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.Schedule
import ru.melowetty.remotescheduleservice.model.ScheduleType

class ScheduleUtils {
    companion object {
        /**
         * Merge different schedules with deleting lessons intersection by schedule priority
         *
         * @param schedules list of schedules
         * @return list of lessons from schedules
         */
        fun mergeSchedules(schedules: List<Schedule>): List<Lesson> {
            val lessons = mutableListOf<Lesson>()
            schedules.asSequence().map {
                if(it.scheduleType == ScheduleType.QUARTER_SCHEDULE) unpackQuarterSchedule(it)
                else listOf(it)
            }.flatten().filter { it.lessons.isNotEmpty() }.groupBy { it.start }.forEach { (_, groupedSchedules) ->
                val foundSchedule = groupedSchedules.maxByOrNull { it.scheduleType.priority }
                if(foundSchedule != null) {
                    lessons.addAll(foundSchedule.lessons)
                }
            }
            return lessons
        }

        fun unpackQuarterSchedule(schedule: Schedule): List<Schedule> {
            val schedules = mutableListOf<Schedule>()
            val lessons = schedule.lessons
            var start = schedule.start.minusDays(schedule.start.dayOfWeek.ordinal.toLong())
            var end = start.plusDays(6)
            while (start.isBefore(schedule.end)) {
                val newSchedule = schedule.copy(
                    start = start,
                    end = end,
                    lessons = lessons.filter { lesson ->
                        lesson.date.isBefore(end) && lesson.date.isAfter(start)
                                || lesson.date == start || lesson.date == end
                    }
                )
                schedules.add(newSchedule)
                start = start.plusDays(7)
                end = end.plusDays(7)
            }
            return schedules
        }
    }
}