package ru.melowetty.remotescheduleservice.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateUtilsTest {
    @Test
    fun `test date time format`() {
        val dateTime = LocalDateTime.of(2024, 10, 24, 10, 5, 13)
        val expected = "24.10.2024 10:05:13"
        val actual = dateTime.format(DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN))

        assertEquals(expected, actual)
    }
}