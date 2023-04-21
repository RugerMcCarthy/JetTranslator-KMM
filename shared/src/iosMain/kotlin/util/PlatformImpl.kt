package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import platform.UIKit.UIPasteboard
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIViewController
import platform.UIKit.endEditing

actual fun hideSoftKeyboard(context: Any) {
    with(context as UIViewController) {
        view().endEditing(true)
    }
}

actual fun copyToClipboard(context: Any, content: String) {
    UIPasteboard.generalPasteboard().apply {
        string = content
    }
}

actual fun detectDarkMode(context: Any): Boolean {
    with(context as UIViewController) {
        val currentThemeStyle = traitCollection.userInterfaceStyle ?: UIUserInterfaceStyle.UIUserInterfaceStyleLight
        return currentThemeStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    }
}

@Composable
actual fun acquirePlatformContext(): Any {
    return LocalUIViewController.current
}
