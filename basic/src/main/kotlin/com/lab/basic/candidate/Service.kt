package com.lab.basic.candidate

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

interface Service {
    fun execute(): String
}

@Component
class ServiceA : Service {
    override fun execute(): String {
        return "ServiceA"
    }
}

@Component
class ServiceB : Service {
    override fun execute(): String {
        return "ServiceB"
    }
}

@Component
class ClientA(
    @Qualifier("serviceA") private val service: Service
) {
    fun performAction() {
        println(service.execute())
    }
}

@Component
class ClientB(
    @Qualifier("serviceB") private val service: Service
) {
    fun performAction() {
        println(service.execute())
    }
}