package com.lab.basic.conditional

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Configuration
class ProfileConditionalConfig {

    @Bean
    @Profile("local", "develop")
    fun debugLogger(): Logger {
        return DebugLogger()
    }

    @Bean
    @Profile("qa", "stage", "prod")
    fun productionLogger(): Logger {
        return ProductionLogger()
    }

    @Bean
    @Profile("!prod")
    fun developmentDataSource(): DataSource {
        return H2DataSource()
    }

    @Bean
    @Profile("prod")
    fun productionDataSource(): DataSource {
        return MySQLDataSource()
    }

    @Bean
    @Conditional(LocalProfileCondition::class)
    fun localOnlyService(): LocalOnlyService {
        return LocalOnlyServiceImpl()
    }

    @Bean
    @Conditional(DevelopmentProfileCondition::class)
    fun developmentToolsService(): DevelopmentToolsService {
        return DevelopmentToolsServiceImpl()
    }

    @Bean
    @Conditional(ProductionProfileCondition::class)
    fun monitoringService(): MonitoringService {
        return MonitoringServiceImpl()
    }
}

@Component
@Profile("local")
class LocalConfigurationService {
    init {
        println("Local configuration service initialized")
    }

    fun getLocalConfig(): Map<String, String> {
        return mapOf(
            "debug" to "true",
            "mock-external-services" to "true",
            "log-level" to "DEBUG"
        )
    }
}

@Component
@Profile("develop")
class DevelopmentConfigurationService {
    init {
        println("Development configuration service initialized")
    }

    fun getDevelopConfig(): Map<String, String> {
        return mapOf(
            "debug" to "true",
            "mock-external-services" to "false",
            "log-level" to "INFO"
        )
    }
}

@Component
@Profile("prod")
class ProductionConfigurationService {
    init {
        println("Production configuration service initialized")
    }

    fun getProdConfig(): Map<String, String> {
        return mapOf(
            "debug" to "false",
            "mock-external-services" to "false",
            "log-level" to "WARN"
        )
    }
}

class DevelopmentProfileCondition : BaseProfileCondition() {
    override fun getTargetProfiles(): List<com.lab.basic.conditional.Profile> = listOf(com.lab.basic.conditional.Profile.LOCAL, com.lab.basic.conditional.Profile.DEVELOP)
}

class ProductionProfileCondition : BaseProfileCondition() {
    override fun getTargetProfiles(): List<com.lab.basic.conditional.Profile> = listOf(com.lab.basic.conditional.Profile.QA, com.lab.basic.conditional.Profile.STAGE, com.lab.basic.conditional.Profile.PROD)
}

interface Logger {
    fun log(message: String)
    fun debug(message: String)
    fun error(message: String)
}

class DebugLogger : Logger {
    override fun log(message: String) {
        println("[DEBUG] $message")
    }

    override fun debug(message: String) {
        println("[DEBUG] $message")
    }

    override fun error(message: String) {
        println("[ERROR] $message")
    }
}

class ProductionLogger : Logger {
    override fun log(message: String) {
        println("[INFO] $message")
    }

    override fun debug(message: String) {
        // No debug logs in production
    }

    override fun error(message: String) {
        println("[ERROR] $message")
        // Send to monitoring system
    }
}

interface DataSource {
    fun getConnection(): String
}

class H2DataSource : DataSource {
    override fun getConnection(): String {
        return "H2 in-memory database connection"
    }
}

class MySQLDataSource : DataSource {
    override fun getConnection(): String {
        return "MySQL production database connection"
    }
}

interface LocalOnlyService {
    fun performLocalOperation(): String
}

class LocalOnlyServiceImpl : LocalOnlyService {
    override fun performLocalOperation(): String {
        return "Local development operation completed"
    }
}

interface DevelopmentToolsService {
    fun generateTestData(): List<String>
    fun clearCache(): String
}

class DevelopmentToolsServiceImpl : DevelopmentToolsService {
    override fun generateTestData(): List<String> {
        return listOf("test-data-1", "test-data-2", "test-data-3")
    }

    override fun clearCache(): String {
        return "Development cache cleared"
    }
}

interface MonitoringService {
    fun reportMetrics(): String
    fun healthCheck(): Boolean
}

class MonitoringServiceImpl : MonitoringService {
    override fun reportMetrics(): String {
        return "Production metrics reported"
    }

    override fun healthCheck(): Boolean {
        return true
    }
}