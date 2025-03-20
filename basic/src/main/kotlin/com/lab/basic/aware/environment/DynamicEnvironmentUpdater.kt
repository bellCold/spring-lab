package com.lab.basic.aware.environment

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.stereotype.Component

// 실행 중 특정 설정을 실시간으로 변경
@Component
class DynamicEnvironmentUpdater(private val environment: ConfigurableEnvironment) {

    fun updateProperty(key: String, value: String) {

    }
}