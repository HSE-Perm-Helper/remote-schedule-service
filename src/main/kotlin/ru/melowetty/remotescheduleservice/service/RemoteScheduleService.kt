package ru.melowetty.remotescheduleservice.service

interface RemoteScheduleService {
    fun getRemoteScheduleAsText(token: String): String
}