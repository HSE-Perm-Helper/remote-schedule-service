package ru.melowetty.remotescheduleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RemoteScheduleServiceApplication

fun main(args: Array<String>) {
    runApplication<RemoteScheduleServiceApplication>(*args)
}
