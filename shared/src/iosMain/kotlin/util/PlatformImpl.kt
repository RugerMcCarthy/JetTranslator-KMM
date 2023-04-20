package util

import platform.UIKit.UIPasteboard
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.endEditing
import viewController


actual fun hideSoftKeyboard() {
    viewController?.view()?.endEditing(true)
}

actual fun copyToClipboard(content: String) {
    UIPasteboard.generalPasteboard().apply {
        string = content
    }
}

actual fun detectDarkMode(): Boolean {
    val currentThemeStyle = viewController?.traitCollection?.userInterfaceStyle ?: UIUserInterfaceStyle.UIUserInterfaceStyleLight
    return currentThemeStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}
