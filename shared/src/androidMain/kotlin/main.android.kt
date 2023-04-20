import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun getPlatformName(): String = "Android"
actual class Log {
    actual companion object {
        actual fun d(msg: String) {
            Log.d("jet_translator", msg)
        }
    }
}

actual val httpClient = HttpClient(OkHttp) {
    engine {
        threadsCount = 4
        config {
            followRedirects(true)
        }
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

object AndroidContextWrapper {
    var context: Activity? = null
    @Composable
    fun renderContent() {
        App()
    }
}
