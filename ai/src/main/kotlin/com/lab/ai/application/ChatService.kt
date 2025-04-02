package com.lab.ai.application

import com.lab.ai.api.AnthropicChatRequest
import com.lab.ai.api.OpenAiChatRequest
import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.anthropic.api.AnthropicApi
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val openAiApi: OpenAiApi,
    private val anthropicApi: AnthropicApi
) {

    fun getOpenAiChatResponse(openAiChatRequest: OpenAiChatRequest): ChatResponse {
        // 메시지 구성
        val messages = listOf(
            SystemMessage("You are a helpful assistant."),
            UserMessage(openAiChatRequest.userInput)
        )

        // 챗 옵션 설정
        val chatOptions = ChatOptions.builder()
            .model("gpt-3.5-turbo")
            .temperature(openAiChatRequest.temperature)
            .stopSequences(openAiChatRequest.stop)
            .build()

        // 프롬프트 생성
        val prompt = Prompt(messages, chatOptions)

        // 챗 모델 생성
        val chatModel = OpenAiChatModel.builder()
            .openAiApi(openAiApi)
            .build()

        // 챗 모델 호출
        return chatModel.call(prompt)
    }

    fun getAnthropicChatResponse(anthropicChatRequest: AnthropicChatRequest): ChatResponse {
        val messages = listOf(
            SystemMessage("You are a helpful assistant."),
            UserMessage(anthropicChatRequest.userInput)
        )

        val chatOptions = ChatOptions.builder()
            .model("claude-3-7-sonnet-20250219")
            .temperature(anthropicChatRequest.temperature)
            .stopSequences(anthropicChatRequest.stop)
            .build()

        val prompt = Prompt(messages, chatOptions)

        val chatModel = AnthropicChatModel.builder()
            .anthropicApi(anthropicApi)
            .build()

        return chatModel.call(prompt)
    }
}