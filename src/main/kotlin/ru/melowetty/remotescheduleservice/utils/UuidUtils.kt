package ru.melowetty.remotescheduleservice.utils

import java.util.UUID

class UuidUtils {
    companion object {
        fun generateUUIDbySeed(seed: String): UUID {
            return UUID.nameUUIDFromBytes(seed.toByteArray())
        }
    }
}