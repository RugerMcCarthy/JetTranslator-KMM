package edu.bupt.jetdeepl.model

import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.path


class GptRepo(private var httpClient: HttpClient) {
    val hostUrl = "ruger.life"
    suspend fun translateByAPI(originWord: String, sourceLanguageCode: String, targetLanguageCode: String, block: suspend (response: HttpResponse) -> Unit) {
        var prompt = if (sourceLanguageCode == "自动检测") {
            "你是一个翻译工具，请将下面这个句子翻译为${targetLanguageCode},"
        } else {
            "你是一个翻译工具，请将下面这个${sourceLanguageCode}句子翻译为${targetLanguageCode},"
        }
        prompt += "并且只输出翻译结果, 对于一切翻译出错的情况则只输出翻译出错的字样: ${originWord}"
        val httpResponse = httpClient.post {
            method = HttpMethod.Post
            url {
                protocol = URLProtocol.HTTPS
                host = hostUrl
                path("/api/openai")
            }
            headers {
                append("access-code", "")
                append("path", "v1/chat/completions")
                append("Content-Type", "application/json")
            }

            setBody(
                "{\n" +
                    "  \"messages\": [\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": \"$prompt\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"model\": \"gpt-3.5-turbo\",\n" +
                    "  \"temperature\": 1,\n" +
                    "  \"max_tokens\": 2000,\n" +
                    "  \"presence_penalty\": 0\n" +
                    "}"
            )
        }
        block(httpResponse)
//        val url: String = if(sourceLanguageCode == "") "https://api-free.deepl.com/v2/translate?auth_key=&text=$originWord&detected_source_language=auto&target_lang=${targetLanguageCode}"
//        else "https://api-free.deepl.com/v2/translate?auth_key=&text=$originWord&source_lang=${sourceLanguageCode}&target_lang=${targetLanguageCode}"
//        val request = Request.Builder()
//            .url(url)
//            .build()
//        okHttpClient.newCall(request).execute().use {
//            block(it)
//        }
    }
}