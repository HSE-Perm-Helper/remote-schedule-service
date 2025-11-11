package ru.melowetty.remotescheduleservice.service

import ru.melowetty.remotescheduleservice.model.Lesson

interface TimetableService {
    fun getUserLessons(telegramId: Long): List<Lesson>
}