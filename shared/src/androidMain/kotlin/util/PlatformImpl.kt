package util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual fun hideSoftKeyboard(context: Any) {
    with(context as Activity) {
        with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
            hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }
}

actual fun copyToClipboard(context: Any, content: String) {
    with(context as Activity) {
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("translate", content)
        clipboardManager.setPrimaryClip(clipData)
    }
}

actual fun detectDarkMode(context: Any): Boolean {
    with(context as Activity) {
        val nightModeFlags: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}

@Composable
actual fun acquirePlatformContext(): Any {
    return LocalContext.current
}
