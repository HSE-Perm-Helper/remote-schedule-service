package ru.melowetty.remotescheduleservice.service.impl

import org.springframework.stereotype.Service
import ru.melowetty.remotescheduleservice.service.RemoteScheduleService
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

@Service
class RemoteScheduleServiceImpl: RemoteScheduleService {
    override fun getRemoteScheduleAsText(telegramId: Long): String {
        return InputStreamReader(FileInputStream("schedule.ics")).readText()
    }
}