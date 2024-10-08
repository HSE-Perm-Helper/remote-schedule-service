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
import ru.melowetty.remotescheduleservice.repository.ScheduleRepository
import ru.melowetty.remotescheduleservice.repository.response.ExternalSchedule
import ru.melowetty.remotescheduleservice.repository.response.ExternalScheduleInfo
import ru.melowetty.remotescheduleservice.repository.response.Response
import ru.melowetty.remotescheduleservice.service.impl.ScheduleServiceImpl

@ExtendWith(MockitoExtension::class)
class ScheduleServiceTest {
    @Mock
    private lateinit var scheduleRepository: ScheduleRepository

    @InjectMocks
    private lateinit var scheduleService: ScheduleServiceImpl

    @Test
    fun getUserLessons() {
        val schedule = ExternalSchedule(
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

        `when`(scheduleRepository.getAvailableSchedules())
            .thenReturn(
                Response(
                    false, listOf(
                        ExternalScheduleInfo(
                            1, LocalDate.of(2024, 10, 7),
                            LocalDate.of(2024, 10, 13), scheduleType = ScheduleType.WEEK_SCHEDULE
                        ),
                        ExternalScheduleInfo(
                            number = 1,
                            start = LocalDate.of(2024, 9, 2),
                            end = LocalDate.of(2024, 10, 24),
                            scheduleType = ScheduleType.QUARTER_SCHEDULE
                        )
                    )
                )
            )

        `when`(
            scheduleRepository.getUserSchedule(
                eq(123L),
                eq(LocalDate.of(2024, 10, 7)),
                eq(LocalDate.of(2024, 10, 13))
            )
        )
            .thenReturn(Response(false, schedule))

        val actual = scheduleService.getUserLessons(123)
        val expected = schedule.lessons

        verify(scheduleRepository, never()).getUserSchedule(
            eq(123L),
            eq(LocalDate.of(2024, 9, 2)),
            eq(LocalDate.of(2024, 10, 24))
        )

        Assertions.assertEquals(expected, actual)
    }
}