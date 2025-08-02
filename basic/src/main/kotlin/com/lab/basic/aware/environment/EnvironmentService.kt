package com.lab.basic.aware.environment

import org.springframework.stereotype.Service

@Service
class EnvironmentService(
    private val environmentAwareBean: EnvironmentAwareBean
) {
    
    fun getEnvironmentInfo(): Map<String, Any> {
        return mapOf(
            "activeProfiles" to environmentAwareBean.getActiveProfiles().toList(),
            "applicationName" to environmentAwareBean.getProperty("spring.application.name", "Unknown"),
            "customMessage" to environmentAwareBean.getProperty("app.custom.message", "No message"),
            "appVersion" to environmentAwareBean.getProperty("app.custom.version", "Unknown"),
            "debugMode" to environmentAwareBean.getProperty("app.custom.debug", "false"),
            "javaVersion" to environmentAwareBean.getProperty("java.version", "Unknown")
        )
    }
    
    fun isProfileActive(profile: String): Boolean {
        return environmentAwareBean.acceptsProfiles(profile)
    }
    
    fun getCustomProperty(key: String, defaultValue: String = ""): String {
        return environmentAwareBean.getProperty(key, defaultValue)
    }
    
    fun hasProperty(key: String): Boolean {
        return environmentAwareBean.containsProperty(key)
    }
    
    fun printEnvironmentSummary() {
        println("\n=== Environment Service Summary ===")
        val info = getEnvironmentInfo()
        info.forEach { (key, value) ->
            println("$key: $value")
        }
        println("===================================\n")
    }
}
