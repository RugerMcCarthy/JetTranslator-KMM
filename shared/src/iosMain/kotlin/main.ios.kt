import androidx.compose.ui.window.ComposeUIViewController
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
        configureSession {
            setTimeoutIntervalForRequest(10000.0)
            setTimeoutIntervalForResource(10000.0)
        }
    }
    install(ContentNegotiation) {
        json()
    }
}

var viewController: UIViewController? = null
fun MainViewController(): UIViewController {
    viewController = ComposeUIViewController { App() }
    return viewController!!
}
