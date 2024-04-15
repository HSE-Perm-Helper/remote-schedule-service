package ru.melowetty.remotescheduleservice.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.core.env.get

class InternalFeignClientConfiguration(
    private val env: Environment
) {
    private val privateKey = env["app.security.private-key"] ?: ""
    @Bean
    fun requestInterceptor(): RequestInterceptor = RequestInterceptor { template ->
        template.header("X-Secret-Key", privateKey)
    }
}