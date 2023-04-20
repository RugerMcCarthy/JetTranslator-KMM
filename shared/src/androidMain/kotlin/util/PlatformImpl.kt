package util

import AndroidContextWrapper
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager


actual fun hideSoftKeyboard() {
    AndroidContextWrapper.context?.apply {
        with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
            hideSoftInputFromWindow(window.decorView. windowToken, 0)
        }
    }
}

actual fun copyToClipboard(content: String) {
    AndroidContextWrapper.context?.apply {
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("translate", content)
        clipboardManager.setPrimaryClip(clipData)
    }
}


actual fun detectDarkMode(): Boolean {
    AndroidContextWrapper.context?.apply {
        val nightModeFlags: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
    return false
}