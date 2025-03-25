package com.lab.basic.conditional

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration

class BaseProfileConditionTest {
    @Test
    fun `local 환경`() {
        val ac = AnnotationConfigApplicationContext()
        ac.environment.setActiveProfiles(Profile.LOCAL.value)
        ac.register(ConditionalBean::class.java)
        ac.refresh()

        assertThat(ac.beanDefinitionNames.contains("localBean")).isTrue
        assertThat(ac.beanDefinitionNames.contains("notLocalDevelopBean")).isFalse
    }

    @Configuration
    class ConditionalBean {
        @Bean
        @Conditional(LocalProfileCondition::class)
        fun localBean(): ConditionalBean = ConditionalBean()
    }
}