package ru.melowetty.remotescheduleservice.utils

import org.junit.jupiter.api.Test
import org.springframework.util.Assert
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.LessonType
import ru.melowetty.remotescheduleservice.model.Schedule
import ru.melowetty.remotescheduleservice.model.ScheduleType
import java.time.LocalDate

class ScheduleUtilsTests {
    @Test
    fun `should unpack quarter schedule for two weeks in two schedules for one week duration`() {
        val baseLesson = Lesson(
            subject = "Test",
            date = LocalDate.of(2024, 1, 8),
            startTime = "9:40",
            endTime = "11:00",
            lessonType = LessonType.TEST,
            parentScheduleType = ScheduleType.QUARTER_SCHEDULE,
            isOnline = false,
            lecturer = null,
        )
        val lessons = listOf(
            baseLesson,
            baseLesson.copy(date = LocalDate.of(2024, 1, 13)),
            baseLesson.copy(date = LocalDate.of(2024, 1, 14)),
            baseLesson.copy(date = LocalDate.of(2024, 1, 15)),
            baseLesson.copy(date = LocalDate.of(2024, 1, 20)),
            baseLesson.copy(date = LocalDate.of(2024, 1, 21)),
        )
        val schedule = Schedule(
            lessons = lessons,
            start = LocalDate.of(2024, 1, 8),
            end = LocalDate.of(2024, 1, 21),
            scheduleType = ScheduleType.QUARTER_SCHEDULE
        )
        val actualSchedules = ScheduleUtils.unpackQuarterSchedule(schedule)
        val expectedSchedules = listOf(
            schedule.copy(
                start = LocalDate.of(2024, 1, 8),
                end = LocalDate.of(2024, 1, 14),
                lessons = listOf(lessons[0], lessons[1], lessons[2])
            ),
            schedule.copy(
                start = LocalDate.of(2024, 1, 15),
                end = LocalDate.of(2024, 1, 21),
                lessons = listOf(lessons[3], lessons[4], lessons[5])
            )
        )
        Assert.isTrue(actualSchedules == expectedSchedules, "schedules does not equal, but they must be equal")
    }

    @Test
    fun `should merge lessons from two week schedule, when input data is quarter schedule and two week schedule`() {
        val baseLesson = Lesson(
            subject = "Test",
            date = LocalDate.of(2024, 1, 8),
            startTime = "9:40",
            endTime = "11:00",
            lessonType = LessonType.TEST,
            parentScheduleType = ScheduleType.QUARTER_SCHEDULE,
            isOnline = false,
            lecturer = null,
        )
        val quarterSchedule = Schedule(
            lessons = listOf(
                baseLesson,
                baseLesson.copy(date = LocalDate.of(2024, 1, 15))
            ),
            scheduleType = ScheduleType.QUARTER_SCHEDULE,
            start = LocalDate.of(2024, 1, 8),
            end = LocalDate.of(2024, 1, 21),
        )
        val anotherLesson = baseLesson.copy(subject = "test2", lecturer = "test")
        val firstWeekSchedule = Schedule(
            lessons = listOf(
                anotherLesson
            ),
            scheduleType = ScheduleType.COMMON_WEEK_SCHEDULE,
            start = LocalDate.of(2024, 1, 8),
            end = LocalDate.of(2024, 1, 14),
        )
        val secondWeekSchedule = Schedule(
            lessons = listOf(
                anotherLesson.copy(date = LocalDate.of(2024, 1, 15))
            ),
            start = LocalDate.of(2024, 1, 15),
            end = LocalDate.of(2024, 1, 21),
            scheduleType = ScheduleType.COMMON_WEEK_SCHEDULE
        )
        val mergedLessons = ScheduleUtils.mergeSchedules(listOf(quarterSchedule, firstWeekSchedule, secondWeekSchedule))
        val expectedLessons = listOf(anotherLesson, anotherLesson.copy(date = LocalDate.of(2024, 1, 15)))
        Assert.isTrue(mergedLessons.sortedBy { it.subject } == expectedLessons.sortedBy { it.subject }, "Merged lessons are not equal to expected lessons after merge")
    }

    @Test
    fun `should merge one schedule in list of lessons from this schedule`() {
        val lesson = Lesson(
            subject = "Test",
            date = LocalDate.of(2024, 1, 8),
            startTime = "9:40",
            endTime = "11:00",
            lessonType = LessonType.TEST,
            parentScheduleType = ScheduleType.QUARTER_SCHEDULE,
            isOnline = false,
            lecturer = null,
        )
        val schedule = Schedule(
            lessons = listOf(
                lesson
            ),
            scheduleType = ScheduleType.COMMON_WEEK_SCHEDULE,
            start = LocalDate.of(2024, 1, 8),
            end = LocalDate.of(2024, 1, 14),
        )
        val mergedLessons = ScheduleUtils.mergeSchedules(listOf(schedule))
        val expectedLessons = listOf(lesson)
        Assert.isTrue(mergedLessons.sortedBy { it.subject } == expectedLessons.sortedBy { it.subject }, "Merged lessons are not equal to expected lessons after merge from schedule")
    }
}