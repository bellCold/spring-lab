package com.lab.basic.conditional

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PropertyConditionalConfig {

    @Bean
    @ConditionalOnProperty(
        name = ["feature.email.enabled"],
        havingValue = "true",
        matchIfMissing = false
    )
    fun emailService(): EmailService {
        return EmailServiceImpl()
    }

    @Bean
    @ConditionalOnProperty(
        name = ["feature.sms.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun smsService(): SmsService {
        return SmsServiceImpl()
    }

    @Bean
    @ConditionalOnProperty(
        name = ["notification.type"],
        havingValue = "email"
    )
    fun emailNotificationHandler(emailService: EmailService): NotificationHandler {
        return EmailNotificationHandler(emailService)
    }

    @Bean
    @ConditionalOnProperty(
        name = ["notification.type"],
        havingValue = "sms"
    )
    fun smsNotificationHandler(smsService: SmsService): NotificationHandler {
        return SmsNotificationHandler(smsService)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "cache",
        name = ["type"],
        havingValue = "redis"
    )
    fun redisCacheManager(): CacheManager {
        return RedisCacheManager()
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "cache",
        name = ["type"],
        havingValue = "memory",
        matchIfMissing = true
    )
    fun memoryCacheManager(): CacheManager {
        return MemoryCacheManager()
    }
}

interface EmailService {
    fun send(to: String, subject: String, body: String)
}

class EmailServiceImpl : EmailService {
    override fun send(to: String, subject: String, body: String) {
        println("Sending email to $to: $subject")
    }
}

interface SmsService {
    fun send(to: String, message: String)
}

class SmsServiceImpl : SmsService {
    override fun send(to: String, message: String) {
        println("Sending SMS to $to: $message")
    }
}

interface NotificationHandler {
    fun handle(message: String)
}

class EmailNotificationHandler(private val emailService: EmailService) : NotificationHandler {
    override fun handle(message: String) {
        emailService.send("admin@example.com", "Notification", message)
    }
}

class SmsNotificationHandler(private val smsService: SmsService) : NotificationHandler {
    override fun handle(message: String) {
        smsService.send("010-1234-5678", message)
    }
}

interface CacheManager {
    fun get(key: String): Any?
    fun put(key: String, value: Any)
}

class RedisCacheManager : CacheManager {
    override fun get(key: String): Any? {
        println("Getting from Redis cache: $key")
        return null
    }

    override fun put(key: String, value: Any) {
        println("Putting to Redis cache: $key = $value")
    }
}

class MemoryCacheManager : CacheManager {
    private val cache = mutableMapOf<String, Any>()

    override fun get(key: String): Any? {
        println("Getting from memory cache: $key")
        return cache[key]
    }

    override fun put(key: String, value: Any) {
        println("Putting to memory cache: $key = $value")
        cache[key] = value
    }
}