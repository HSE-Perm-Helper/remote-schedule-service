package ru.melowetty.remotescheduleservice.controller

import java.nio.charset.Charset
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.melowetty.remotescheduleservice.service.RemoteScheduleService

@RestController
@RequestMapping("remote-schedule")
class RemoteScheduleController(
    private val remoteScheduleService: RemoteScheduleService
) {
    @GetMapping
    fun getRemoteSchedule(
        @RequestParam("telegramId") telegramId: Long,
        @RequestParam token: String
    ): ResponseEntity<ByteArray> {
        return ResponseEntity.ok()
            .contentType(MediaType.asMediaType(MimeType("text", "calendar", Charset.forName("UTF-8"))))
            .body(remoteScheduleService.getRemoteScheduleAsText(telegramId, token).toByteArray())
    }
}