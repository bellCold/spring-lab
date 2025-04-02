package com.lab.ai.api

import com.lab.ai.application.ChatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(private val chatService: ChatService) {
    @GetMapping("/open-ai")
    fun getOpenAiChatResponse(@RequestBody openAiChatRequest: OpenAiChatRequest) {
        chatService.getOpenAiChatResponse(openAiChatRequest)
    }

    @GetMapping("/anthropic")
    fun getAnthropicChatResponse(@RequestBody anthropicChatRequest: AnthropicChatRequest) {
        chatService.getAnthropicChatResponse(anthropicChatRequest)
    }
}

sealed class ChatRequest {
    abstract val userInput: String
    abstract val stop: List<String>
    abstract val temperature: Double
}

data class OpenAiChatRequest(
    override val userInput: String,
    override val stop: List<String>,
    override val temperature: Double
) : ChatRequest()

data class AnthropicChatRequest(
    override val userInput: String,
    override val stop: List<String>,
    override val temperature: Double
) : ChatRequest()