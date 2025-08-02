package com.lab.basic.aware.environment

import jakarta.annotation.PostConstruct
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class EnvironmentAwareBean : EnvironmentAware {

    private lateinit var environment: Environment

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
        println("Environment has been set in EnvironmentAwareBean")
    }

    @PostConstruct
    fun init() {
        println("=== Environment Information ===")

        // 활성 프로파일 확인
        val activeProfiles = environment.activeProfiles
        println("Active Profiles: ${activeProfiles.contentToString()}")

        // 기본 프로파일 확인
        val defaultProfiles = environment.defaultProfiles
        println("Default Profiles: ${defaultProfiles.contentToString()}")

        // 시스템 속성 확인
        val javaVersion = environment.getProperty("java.version")
        println("Java Version: $javaVersion")

        // 환경 변수 확인
        val javaHome = environment.getProperty("JAVA_HOME")
        println("Java Home: $javaHome")

        // 애플리케이션 속성 확인
        val appName = environment.getProperty("spring.application.name", "Unknown")
        println("Application Name: $appName")

        // 커스텀 속성 확인
        val customProperty = environment.getProperty("app.custom.message", "Default Message")
        println("Custom Message: $customProperty")

        println("===============================")
    }

    fun getActiveProfiles(): Array<String> = environment.activeProfiles

    fun getProperty(key: String): String? = environment.getProperty(key)

    fun getProperty(key: String, defaultValue: String): String =
        environment.getProperty(key, defaultValue)

    fun containsProperty(key: String): Boolean = environment.containsProperty(key)

    fun acceptsProfiles(vararg profiles: String): Boolean =
        environment.acceptsProfiles(*profiles)
}