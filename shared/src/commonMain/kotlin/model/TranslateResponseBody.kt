package model

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponseBody(
    var id: String,
    var `object`: String,
    var created: String,
    var model: String,
    var usage: Usage,
    var choices: List<Choice>
)

@Serializable
data class Usage(
    var prompt_tokens: String,
    var completion_tokens: String,
    var total_tokens: String
)

@Serializable
data class Message (
    var role: String,
    var content: String
)

@Serializable
data class Choice (
    var message: Message,
    var finish_reason: String,
    var index: Int
)