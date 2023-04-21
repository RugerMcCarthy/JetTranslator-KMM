package util

import androidx.compose.runtime.Composable

expect fun hideSoftKeyboard(context: Any)

expect fun copyToClipboard(context: Any, content: String)

expect fun detectDarkMode(context: Any): Boolean

@Composable
expect fun acquirePlatformContext(): Any
