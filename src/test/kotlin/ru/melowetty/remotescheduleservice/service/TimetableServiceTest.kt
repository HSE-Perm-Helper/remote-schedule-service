package ru.melowetty.remotescheduleservice.service

import java.time.LocalDate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.LessonPlace
import ru.melowetty.remotescheduleservice.model.LessonTime
import ru.melowetty.remotescheduleservice.model.LessonType
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.repository.TimetableRepository
import ru.melowetty.remotescheduleservice.repository.response.ExternalTimetable
import ru.melowetty.remotescheduleservice.repository.response.ExternalScheduleInfo
import ru.melowetty.remotescheduleservice.service.impl.TimetableServiceImpl

@ExtendWith(MockitoExtension::class)
class TimetableServiceTest {
    @Mock
    private lateinit var timetableRepository: TimetableRepository

    @InjectMocks
    private lateinit var timetableService: TimetableServiceImpl

    @Test
    fun getUserLessons() {
        val telegramId = 123L

        val schedule = ExternalTimetable(
            id = "1",
            number = 6,
            start = LocalDate.of(2024, 10, 7),
            end = LocalDate.of(2024, 10, 13),
            scheduleType = ScheduleType.WEEK_SCHEDULE,
            lessons = listOf(
                Lesson(
                    subject = "Лидерство и управление командой",
                    time = LessonTime(
                        date = LocalDate.of(2024, 10, 7),
                        startTime = "11:30",
                        endTime = "12:50"
                    ),
                    lecturer = "Грабарь В.В.",
                    places = listOf(
                        LessonPlace(office = "102", building = 3)
                    ),
                    lessonType = LessonType.LECTURE,
                    parentScheduleType = ScheduleType.WEEK_SCHEDULE,
                    isOnline = false,
                )
            )
        )

        `when`(timetableRepository.getAvailableTimetables(telegramId))
            .thenReturn(
                listOf(
                    ExternalScheduleInfo(
                        "1", 1, LocalDate.of(2024, 10, 7),
                        LocalDate.of(2024, 10, 13), scheduleType = ScheduleType.WEEK_SCHEDULE
                    ),
                    ExternalScheduleInfo(
                        "2",
                        number = 1,
                        start = LocalDate.of(2024, 9, 2),
                        end = LocalDate.of(2024, 10, 24),
                        scheduleType = ScheduleType.QUARTER_SCHEDULE
                    )
                )
            )

        `when`(
            timetableRepository.getTimetable(
                eq(telegramId),
                eq("1")
            )
        )
            .thenReturn(schedule)

        val actual = timetableService.getUserLessons(123)
        val expected = schedule.lessons

        verify(timetableRepository, never()).getTimetable(
            eq(telegramId),
            eq("2")
        )

        Assertions.assertEquals(expected, actual)
    }
}