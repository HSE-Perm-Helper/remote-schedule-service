package ru.melowetty.remotescheduleservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.melowetty.remotescheduleservice.entity.CalendarTokenEntity

@Repository
interface CalendarTokenRepository : JpaRepository<CalendarTokenEntity, Long> {
    fun findByToken(token: String): CalendarTokenEntity?
}