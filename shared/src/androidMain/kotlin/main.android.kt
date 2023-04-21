
import android.content.Context
import android.util.Log
import com.liftric.kvault.KVault
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
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }
}
lateinit var localEnvStore: KVault
actual fun getLocalEnvStore(context: Any): KVault {
    if (!::localEnvStore.isInitialized) {
        localEnvStore = KVault(context as Context, "translator")
    }
    return localEnvStore
}
