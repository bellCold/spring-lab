package com.lab.basic.candidate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

class ClientATest {

    @Test
    fun testClientAAndB() {
        val context = AnnotationConfigApplicationContext(AppConfig::class.java)

        val clientA = context.getBean(ClientA::class.java)
        val clientB = context.getBean(ClientB::class.java)

        val clientAResult = clientA.performAction()
        assertThat(clientAResult).isEqualTo("ServiceA")

        val clientBResult = clientB.performAction()
        assertThat(clientBResult).isEqualTo("ServiceB")

        context.close()
    }

    @Configuration
    @ComponentScan(basePackages = ["com.lab.basic.candidate"])
    class AppConfig
}
