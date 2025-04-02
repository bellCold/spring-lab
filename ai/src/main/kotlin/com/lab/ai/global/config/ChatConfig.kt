package com.lab.ai.global.config

import org.springframework.ai.anthropic.api.AnthropicApi
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatConfig {
    @Bean
    fun openAiApi(@Value("\${spring.ai.openai.api-key}") apiKey: String): OpenAiApi {
        return OpenAiApi.builder()
            .apiKey(apiKey)
            .build()
    }

    @Bean
    fun anthropicApi(): AnthropicApi {
        return AnthropicApi("YOUR_ANTHROPIC_API_KEY")
    }
}