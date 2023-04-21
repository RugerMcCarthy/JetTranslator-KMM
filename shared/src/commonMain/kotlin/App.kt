import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.liftric.kvault.KVault
import edu.bupt.jetdeepl.model.GptRepo
import io.ktor.client.HttpClient
import model.MainViewModel
import ui.SettingsDrawer
import ui.TranslateLayout
import ui.theme.JetDeepLTheme
import util.acquirePlatformContext

@Composable
fun Home() {
    val context: Any = acquirePlatformContext()
    val scaffoldState = rememberScaffoldState()
    val isDrawerOpen by rememberUpdatedState(scaffoldState.drawerState.isOpen)
    val viewModel = remember { MainViewModel(GptRepo(httpClient), context) }
    LaunchedEffect(isDrawerOpen) {
        if (!isDrawerOpen) {
            viewModel.recordCurrentAccessCode()
        }
    }
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
expect fun getLocalEnvStore(context: Any): KVault

expect class Log {
    companion object {
        fun d(msg: String)
    }
}
