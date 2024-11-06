package ru.melowetty.remotescheduleservice.service

interface RemoteScheduleService {
    fun getRemoteScheduleAsText(telegramId: Long, token: String): String
}