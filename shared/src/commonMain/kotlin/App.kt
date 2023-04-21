import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import edu.bupt.jetdeepl.model.GptRepo
import io.ktor.client.HttpClient
import model.MainViewModel
import ui.SettingsDrawer
import ui.TranslateLayout
import ui.theme.JetDeepLTheme

@Composable
fun Home() {
    val scaffoldState = rememberScaffoldState()
    val viewModel = MainViewModel(GptRepo(httpClient))
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data
                )
            }
        },
        drawerContent = { SettingsDrawer(viewModel) }
    ) {
        TranslateLayout(viewModel, scaffoldState)
    }
}

@Composable
fun App() {
    JetDeepLTheme {
        Home()
    }
}

expect fun getPlatformName(): String

expect val httpClient: HttpClient
expect class Log {
    companion object {
        fun d(msg: String)
    }
}
