package ru.melowetty.remotescheduleservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "token")
data class CalendarTokenEntity(
    @Id
    val telegramId: Long,

    @Column(length = 64, nullable = false)
    val token: String
)
