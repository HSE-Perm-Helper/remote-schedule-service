package ru.melowetty.remotescheduleservice.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class UuidUtilsTest {
    @Test
    fun `generate same uuids by same seed`() {
        val first = UuidUtils.generateUUIDbySeed("123")
        val second = UuidUtils.generateUUIDbySeed("123")
        assertEquals(first, second)
    }

    @Test
    fun `generate different uuids by different seed`() {
        val first = UuidUtils.generateUUIDbySeed("123")
        val second = UuidUtils.generateUUIDbySeed("124")
        assertNotEquals(first, second)
    }
}