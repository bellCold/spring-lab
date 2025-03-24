package com.lab.basic

import com.lab.basic.aware.environment.DynamicEnvironmentUpdater
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BasicApplication

fun main(args: Array<String>) {
    val context = runApplication<BasicApplication>(*args)

    val initialValue = context.environment.getProperty("app.mode")
    println("변경 전 앱 모드: $initialValue")

    val updater = context.getBean(DynamicEnvironmentUpdater::class.java)
    updater.updateProperty("app.mode", "update")

    val updatedValue = context.environment.getProperty("app.mode")
    println("변경 후 앱 모드: $updatedValue")
}