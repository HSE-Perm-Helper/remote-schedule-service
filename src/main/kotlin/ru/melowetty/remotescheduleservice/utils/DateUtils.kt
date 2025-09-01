package ru.melowetty.remotescheduleservice.utils

import java.time.ZoneId
import java.time.ZoneOffset

object DateUtils {
    const val TIME_ZONE_STR = "Asia/Yekaterinburg"
    const val DATE_PATTERN = "dd.MM.yyyy"
    const val TIME_PATTERN = "HH:mm"
    const val DATE_TIME_PATTERN = "$DATE_PATTERN $TIME_PATTERN"
    const val ZONE_OFFSET_STR = "+05:00"

    val timeZone = ZoneId.of(TIME_ZONE_STR)
    val zoneOffset = ZoneOffset.of(ZONE_OFFSET_STR)
}