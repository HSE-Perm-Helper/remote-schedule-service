package ru.melowetty.remotescheduleservice.config

import feign.Feign
import feign.Retryer
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableFeignClients
@Configuration
class FeignConfig {
    @Bean
    fun feignClient(): Feign {
        return Feign.builder()
            .retryer(Retryer.Default())
            .build()
    }
}