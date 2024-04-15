package ru.melowetty.remotescheduleservice.service

import ru.melowetty.remotescheduleservice.model.Lesson

interface ScheduleService {
    fun getUserLessons(telegramId: Long): List<Lesson>
}