package com.lab.basic.conditional

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.AnnotatedTypeMetadata
import java.time.LocalTime

@Configuration
class CustomConditionalConfig {

    @Bean
    @Conditional(TimeBasedCondition::class)
    fun dayTimeService(): TimeBasedService {
        return DayTimeService()
    }

    @Bean
    @Conditional(NightTimeCondition::class)
    fun nightTimeService(): TimeBasedService {
        return NightTimeService()
    }

    @Bean
    @Conditional(OperatingSystemCondition::class)
    fun windowsSpecificService(): OSSpecificService {
        return WindowsService()
    }

    @Bean
    @Conditional(LinuxOrMacCondition::class)
    fun unixBasedService(): OSSpecificService {
        return UnixBasedService()
    }

    @Bean
    @Conditional(MemoryCondition::class)
    fun highPerformanceService(): PerformanceService {
        return HighPerformanceService()
    }

    @Bean
    @Conditional(LowMemoryCondition::class)
    fun lowResourceService(): PerformanceService {
        return LowResourceService()
    }

    @Bean
    @Conditional(ExternalServiceAvailableCondition::class)
    fun externalIntegrationService(): IntegrationService {
        return ExternalIntegrationService()
    }

    @Bean
    @Conditional(ExternalServiceUnavailableCondition::class)
    fun mockIntegrationService(): IntegrationService {
        return MockIntegrationService()
    }

    @Bean
    @Conditional(FeatureToggleCondition::class)
    fun experimentalFeatureService(): FeatureService {
        return ExperimentalFeatureService()
    }

    @Bean
    @Conditional(StableFeatureCondition::class)
    fun stableFeatureService(): FeatureService {
        return StableFeatureService()
    }
}

class TimeBasedCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val currentHour = LocalTime.now().hour
        return currentHour in 6..18
    }
}

class NightTimeCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val currentHour = LocalTime.now().hour
        return currentHour < 6 || currentHour > 18
    }
}

class OperatingSystemCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val osName = System.getProperty("os.name").lowercase()
        return osName.contains("windows")
    }
}

class LinuxOrMacCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val osName = System.getProperty("os.name").lowercase()
        return osName.contains("linux") || osName.contains("mac")
    }
}

class MemoryCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val gb = 1024 * 1024 * 1024
        return maxMemory > 4 * gb
    }
}

class LowMemoryCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val gb = 1024 * 1024 * 1024
        return maxMemory <= 4 * gb
    }
}

class ExternalServiceAvailableCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        return try {
            val externalServiceUrl = context.environment.getProperty("external.service.url")
            externalServiceUrl?.isNotEmpty() == true && pingService(externalServiceUrl)
        } catch (e: Exception) {
            false
        }
    }

    private fun pingService(url: String): Boolean {
        return try {
            true
        } catch (e: Exception) {
            false
        }
    }
}

class ExternalServiceUnavailableCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        return !ExternalServiceAvailableCondition().matches(context, metadata)
    }
}

class FeatureToggleCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val featureEnabled = context.environment.getProperty("feature.experimental.enabled", Boolean::class.java, false)
        val currentDate = System.currentTimeMillis()
        val enabledFromDate = context.environment.getProperty("feature.experimental.enabled-from", Long::class.java, 0L)
        
        return featureEnabled && currentDate >= enabledFromDate
    }
}

class StableFeatureCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        return !FeatureToggleCondition().matches(context, metadata)
    }
}

interface TimeBasedService {
    fun processRequest(): String
}

class DayTimeService : TimeBasedService {
    override fun processRequest(): String {
        return "Processing request during day time - full service available"
    }
}

class NightTimeService : TimeBasedService {
    override fun processRequest(): String {
        return "Processing request during night time - maintenance mode"
    }
}

interface OSSpecificService {
    fun performOSSpecificOperation(): String
}

class WindowsService : OSSpecificService {
    override fun performOSSpecificOperation(): String {
        return "Executing Windows-specific operation"
    }
}

class UnixBasedService : OSSpecificService {
    override fun performOSSpecificOperation(): String {
        return "Executing Unix-based operation"
    }
}

interface PerformanceService {
    fun executeTask(): String
}

class HighPerformanceService : PerformanceService {
    override fun executeTask(): String {
        return "Executing high-performance task with optimizations"
    }
}

class LowResourceService : PerformanceService {
    override fun executeTask(): String {
        return "Executing task with minimal resource usage"
    }
}

interface IntegrationService {
    fun integrate(): String
}

class ExternalIntegrationService : IntegrationService {
    override fun integrate(): String {
        return "Integrating with external service"
    }
}

class MockIntegrationService : IntegrationService {
    override fun integrate(): String {
        return "Using mock integration - external service unavailable"
    }
}

interface FeatureService {
    fun executeFeature(): String
}

class ExperimentalFeatureService : FeatureService {
    override fun executeFeature(): String {
        return "Executing experimental feature"
    }
}

class StableFeatureService : FeatureService {
    override fun executeFeature(): String {
        return "Executing stable feature"
    }
}