package com.lab.basic

import com.lab.basic.aware.environment.DynamicEnvironmentUpdater
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BasicApplication

fun main(args: Array<String>) {
    runApplication<BasicApplication>(*args)
        .getBean(DynamicEnvironmentUpdater::class.java)
}
