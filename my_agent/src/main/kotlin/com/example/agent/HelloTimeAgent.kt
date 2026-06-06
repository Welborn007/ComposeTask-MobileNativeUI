package com.example.agent

import com.google.adk.kt.agents.Instruction
import com.google.adk.kt.agents.LlmAgent
import com.google.adk.kt.annotations.Param
import com.google.adk.kt.annotations.Tool
import com.google.adk.kt.models.Gemini
import java.io.File

class TimeService {
    /** Mock tool implementation */
    @Tool
    fun getCurrentTime(
        @Param("Name of the city to get the time for") city: String
    ): Map<String, String> {
        return mapOf("city" to city, "time" to "The time is 10:30am.")
    }
}

object HelloTimeAgent {
    @JvmField
    val rootAgent = LlmAgent(
        name = "hello_time_agent",
        description = "Tells the current time in a specified city.",
        model = Gemini(
            name = "gemini-flash-latest",
            apiKey = getApiKey(),
        ),
        instruction = Instruction(
            "You are a helpful assistant that tells the current time in a city. "
                    + "Use the 'getCurrentTime' tool for this purpose."
        ),
        tools = TimeService().generatedTools(),
    )

    private fun getApiKey(): String {
        return System.getenv("GOOGLE_API_KEY")
            ?: loadFromDotEnv()
            ?: error("GOOGLE_API_KEY environment variable not set. Please set it or provide it in a .env file in the project root.")
    }

    private fun loadFromDotEnv(): String? {
        val envFile = File(".env").takeIf { it.exists() } ?: File("../.env").takeIf { it.exists() }
        return envFile?.readLines()
            ?.find { it.contains("GOOGLE_API_KEY") }
            ?.substringAfter("=")
            ?.trim()
            ?.removeSurrounding("\"")
    }
}