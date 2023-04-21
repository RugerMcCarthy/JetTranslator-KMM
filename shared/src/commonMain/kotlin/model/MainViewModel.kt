package model

import Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.bupt.jetdeepl.data.AllAvailableLanguages
import edu.bupt.jetdeepl.model.GptRepo
import getLocalEnvStore
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import util.hideSoftKeyboard

sealed class SelectMode {
    object SOURCE : SelectMode()
    object TARGET : SelectMode()
}

class MainViewModel constructor(private var gptRepo: GptRepo, private var context: Any) {
    var displayOutput by mutableStateOf("")
    var displayInput by mutableStateOf("")
    var isTranslatSuccess by mutableStateOf(false)
    var displaySourceLanguage by mutableStateOf("自动检测")
    var displayTargetLanguage by mutableStateOf("中文")
    var flipToggle = false
    var flipEventFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    var localEnvStore = getLocalEnvStore(context)
    var accessCode by mutableStateOf(localEnvStore.string("access_code") ?: "")
    var currentSelectMode: SelectMode by mutableStateOf(SelectMode.SOURCE)
        private set
    var focusOnSearch by mutableStateOf(false)

    var displayLanguageList by mutableStateOf<List<String>>(
        AllAvailableLanguages.keys.toList()
    )

    val viewModelScope = CoroutineScope(EmptyCoroutineContext)
    private val sourceLanguageCode
        get() = displaySourceLanguage
    private val targetLanguageCode
        get() = displayTargetLanguage

    // API 方式
    private fun translateByAPI(originWord: String, translateFlow: MutableSharedFlow<String>) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                gptRepo.translateByAPI(
                    accessCode,
                    originWord,
                    sourceLanguageCode,
                    targetLanguageCode
                ) { response ->
                    if (response.status.value == 200) {
                        Log.d(response.bodyAsText())
                        val body: TranslateResponseBody = response.body()
                        val content = body.choices[0].message.content ?: "出错了~"
                        translateFlow.emit(content.replace("\"", ""))
                        isTranslatSuccess = true
                    } else {
                        translateFlow.emit("似乎网络出现了点问题～")
                    }
                }
            } catch (e: IOException) {
                translateFlow.emit("似乎网络出现了点问题～")
                e.printStackTrace()
            }
        }
    }

    fun translate(platformContext: Any) {
        hideSoftKeyboard(platformContext)
        isTranslatSuccess = false
        val translateFlow = MutableSharedFlow<String>()
        translateByAPI(displayInput, translateFlow)
        viewModelScope.launch {
            val waitingJob = launch {
                var count = 0
                while (true) {
                    if (displayOutput == ".....") {
                        displayOutput = "."
                    } else {
                        displayOutput += "."
                    }
                    delay(500)
                    count++
                    if (count >= 60) {
                        translateFlow.emit("似乎网络出现了点问题～")
                    }
                }
            }
            launch {
                translateFlow.collect {
                    waitingJob.cancel()
                    displayOutput = it
                }
            }
        }
    }

    fun clearOutputDisplay() {
        displayOutput = ""
    }

    fun clearInputDisplay() {
        displayInput = ""
    }

    fun flipLanguage() {
        flipToggle = !flipToggle
        flipEventFlow.tryEmit(Unit)
    }

    fun changeSelectMode(selectMode: SelectMode) {
        currentSelectMode = selectMode
    }

    fun isSelectedLanguage(languageName: String): Boolean {
        return when (currentSelectMode) {
            SelectMode.SOURCE -> {
                displaySourceLanguage == languageName
            }
            SelectMode.TARGET -> {
                displayTargetLanguage == languageName
            }
        }
    }

    fun selectLanguage(languageName: String) {
        when (currentSelectMode) {
            SelectMode.SOURCE -> {
                displaySourceLanguage = languageName
            }
            SelectMode.TARGET -> {
                displayTargetLanguage = languageName
            }
        }
    }

    fun recordCurrentAccessCode() {
        localEnvStore.set("access_code", accessCode)
    }
}
