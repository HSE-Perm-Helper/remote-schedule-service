package ru.melowetty.remotescheduleservice.model

enum class ScheduleType(
    val priority: Int
) {
    QUARTER_SCHEDULE(0),
    COMMON_WEEK_SCHEDULE(1),
    SESSION_WEEK_SCHEDULE(2),
}