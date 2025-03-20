package com.lab.basic.aware.environment

import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class EnvironmentAwareBean : EnvironmentAware {
    override fun setEnvironment(environment: Environment) {
    }
}