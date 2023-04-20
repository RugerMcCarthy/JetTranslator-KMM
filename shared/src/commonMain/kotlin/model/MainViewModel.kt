package model

import Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.bupt.jetdeepl.data.AllAvailableLanguages
import edu.bupt.jetdeepl.model.GptRepo
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.launch
import util.copyToClipboard
import util.hideSoftKeyboard

import kotlin.coroutines.EmptyCoroutineContext

sealed class SelectMode {
    object SOURCE: SelectMode()
    object TARGET: SelectMode()
}

class MainViewModel constructor(private var gptRepo: GptRepo) {
    var displayOutput by mutableStateOf("")
    var displayInput by mutableStateOf("")
    var isTranslatSuccess by mutableStateOf(false)
    var displaySourceLanguage by mutableStateOf("自动检测")
    var displayTargetLanguage by mutableStateOf("中文")
    var flipToggle by mutableStateOf(false)
    var currentSelectMode: SelectMode by mutableStateOf(SelectMode.SOURCE)
        private set
    var focusOnSearch by mutableStateOf(false)

    var displayLanguageList by mutableStateOf<List<String>>(
        AllAvailableLanguages.keys.toList()
    )

    val viewModelScope = CoroutineScope(EmptyCoroutineContext);
    private val sourceLanguageCode
        get() = displaySourceLanguage
    private val targetLanguageCode
        get() = displayTargetLanguage

    // API 方式
    private fun translateByAPI(originWord: String, translateFlow: MutableSharedFlow<String>){
        viewModelScope.launch(Dispatchers.Default){
            try{
                gptRepo.translateByAPI(originWord, sourceLanguageCode, targetLanguageCode) { response ->
                        if(response.status.value == 200) {
                            Log.d(response.bodyAsText())
                            val body: TranslateResponseBody = response.body()
                            val content = body.choices[0].message.content ?: "出错了~"
                            translateFlow.emit(content.replace("\"", ""))
                            isTranslatSuccess = true
                        } else {
                            Log.d("network error")
                        }
                }
            }catch(e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun translate() {
        hideSoftKeyboard()
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
                    if (count == 60) {
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

    fun requestCopyToClipboard() {
        copyToClipboard(displayOutput)
    }
}

