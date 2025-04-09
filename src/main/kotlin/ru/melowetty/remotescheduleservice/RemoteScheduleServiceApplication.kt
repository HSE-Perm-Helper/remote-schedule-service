package ru.melowetty.remotescheduleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class RemoteScheduleServiceApplication

fun main(args: Array<String>) {
    runApplication<RemoteScheduleServiceApplication>(*args)
}
