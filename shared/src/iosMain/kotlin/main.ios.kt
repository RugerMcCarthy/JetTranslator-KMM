
import androidx.compose.ui.window.ComposeUIViewController
import com.liftric.kvault.KVault
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import platform.Foundation.NSLog
import platform.UIKit.UIViewController

actual fun getPlatformName(): String = "iOS"

actual class Log {
    actual companion object {
        actual fun d(msg: String) {
            NSLog(msg)
        }
    }
}

actual val httpClient = HttpClient(Darwin) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    install(ContentNegotiation) {
        json()
    }
}

lateinit var localEnvStore: KVault

actual fun getLocalEnvStore(context: Any): KVault {
    if (!::localEnvStore.isInitialized) {
        localEnvStore = KVault("translator")
    }
    return localEnvStore
}

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
