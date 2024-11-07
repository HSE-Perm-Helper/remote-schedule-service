package ru.melowetty.remotescheduleservice.repository

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.optionals.getOrNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import ru.melowetty.remotescheduleservice.entity.CalendarTokenEntity

@SpringBootTest
@TestPropertySource(
    properties = [
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
    ]
)
@Testcontainers
@ActiveProfiles("test-with-db")
class CalendarTokenRepositoryTest {
    @Autowired
    private lateinit var repository: CalendarTokenRepository

    @AfterEach
    fun clearDb() {
        repository.deleteAll()
    }

    @Test
    fun `create and returns new token entity`() {
        val token = CalendarTokenEntity(telegramId = 123, "1".repeat(64), null)

        repository.save(token)

        val actual = repository.findById(123)
        Assertions.assertNotNull(actual.getOrNull())

        val entity = actual.get()

        Assertions.assertEquals(entity.telegramId, 123)
        Assertions.assertNotNull(entity.token, "1".repeat(64))
    }

    @Test
    fun `delete and no returns in repository`() {
        val token = CalendarTokenEntity(telegramId = 123, "secret", null)

        repository.save(token)

        repository.deleteById(123)
        val actual = repository.findById(123)
        Assertions.assertNull(actual.getOrNull())
    }

    @Test
    fun `create two or more objects with different id`() {
        val tokenFirst = CalendarTokenEntity(telegramId = 123, "secret", null)
        val tokenSecond = CalendarTokenEntity(telegramId = 124, "secret", null)

        repository.save(tokenFirst)
        repository.save(tokenSecond)

        val actual = repository.count()
        Assertions.assertEquals(actual, 2)
    }

    @Test
    fun `save with last fetch field`() {
        val time = LocalDateTime.now(ZoneOffset.UTC)
        val token = CalendarTokenEntity(telegramId = 123, "secret", time)

        repository.save(token)

        val actual = repository.findById(123)
        Assertions.assertNotNull(actual)

        val actualToken = actual.get()

        Assertions.assertEquals(actualToken, token)
    }
}