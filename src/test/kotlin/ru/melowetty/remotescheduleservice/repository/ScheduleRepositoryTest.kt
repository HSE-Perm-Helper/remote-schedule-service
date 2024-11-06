package ru.melowetty.remotescheduleservice.repository

import java.time.LocalDate
import javax.sql.DataSource
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.wiremock.integrations.testcontainers.WireMockContainer
import ru.melowetty.remotescheduleservice.model.Lesson
import ru.melowetty.remotescheduleservice.model.LessonPlace
import ru.melowetty.remotescheduleservice.model.LessonTime
import ru.melowetty.remotescheduleservice.model.LessonType
import ru.melowetty.remotescheduleservice.model.ScheduleType
import ru.melowetty.remotescheduleservice.repository.response.ExternalSchedule
import ru.melowetty.remotescheduleservice.repository.response.ExternalScheduleInfo


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
class ScheduleRepositoryTest {
    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @MockBean
    private lateinit var dataSource: DataSource

    @Test
    fun getSchedulesInfo_returnsSchedulesInfo() {
        runBlocking {
            val actual: List<ExternalScheduleInfo> = scheduleRepository.getAvailableSchedules().response

            val expected = listOf(
                ExternalScheduleInfo(
                    number = 1,
                    start = LocalDate.of(2024, 9, 1),
                    end = LocalDate.of(2024, 10, 24),
                    scheduleType = ScheduleType.QUARTER_SCHEDULE
                ),
                ExternalScheduleInfo(
                    number = 6,
                    start = LocalDate.of(2024, 10, 7),
                    end = LocalDate.of(2024, 10, 13),
                    scheduleType = ScheduleType.WEEK_SCHEDULE
                )
            )

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun getWeekSchedule_returnsWeekSchedule() {
        runBlocking {
            val actual: ExternalSchedule = scheduleRepository
                .getUserSchedule(1, LocalDate.of(2024, 10, 7), LocalDate.of(2024, 10, 13))
                .response

            val expected = ExternalSchedule(
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

            Assertions.assertEquals(expected, actual)
        }
    }

    companion object {
        @Container
        @JvmStatic
        var wireMock: WireMockContainer = WireMockContainer("wiremock/wiremock:3.2.0-alpine")
            .withMappingFromResource(ScheduleRepositoryTest::class.java, "schedules.json")
            .withMappingFromResource(ScheduleRepositoryTest::class.java, "week_lessons.json")

        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("api.schedule-service.url") { wireMock.getUrl("") }
            registry.add("spring.datasource.url") { "" }
        }

    }
}