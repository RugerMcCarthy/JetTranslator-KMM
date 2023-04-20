package ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import util.detectDarkMode

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color(0xfff8f8f8),
    onBackground = Color.White
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JetDeepLTheme(isDarkTheme: Boolean = detectDarkMode(), content: @Composable() () -> Unit) {
    val colors = if (isDarkTheme) DarkColorPalette else LightColorPalette
    val extensionColors = if (isDarkTheme) ExtensionColors.DarkExtensionColors else ExtensionColors.LightExtensionColors
    CompositionLocalProvider(LocalExtensionColors provides extensionColors) {
        MaterialTheme(
            colors = colors,
            content = content
        )
    }
}

open class ExtensionColors private constructor(
    var inputTextColor: Color,
    var inputBackgroundColor: Color,
    var outputTextColor: Color,
    var outputBackgroundColor: Color,
    var toggleLangIconColor: Color,
    var selectLangTextColor: Color,
    var selectLangTextSpecificColor: Color,
    var selectLangIconColor: Color,
    var iconColor: Color,
    var translateColor: Color,
    var inputHintColor: Color,
    var searchLanguageColor: Color,
) {
    object LightExtensionColors : ExtensionColors(
        inputTextColor = Color.Black,
        inputBackgroundColor = Color.White,
        outputTextColor = Color.Black,
        outputBackgroundColor = Color.White,
        toggleLangIconColor = Color.Black,
        selectLangTextColor = Color.Black,
        selectLangTextSpecificColor = Color(0xff99c455),
        selectLangIconColor = Color.Black,
        iconColor = Color(0xff5d5df3),
        translateColor = Color(0xff5d5df3),
        inputHintColor = Color(0xffcccccc),
        searchLanguageColor = Color(0xfff3f4f6),
    )

    object DarkExtensionColors : ExtensionColors(
        inputTextColor = Color.Black,
        inputBackgroundColor = Color.White,
        outputTextColor = Color.Black,
        outputBackgroundColor = Color.White,
        toggleLangIconColor = Color.Black,
        selectLangTextColor = Color.White,
        selectLangTextSpecificColor = Color(0xff99c455),
        selectLangIconColor = Color.White,
        iconColor = Color(0xffffffff),
        translateColor = Color(0xff5d5df3),
        inputHintColor = Color(0xffcccccc),
        searchLanguageColor = Color(0xfff3f4f6),
    )
}

internal var LocalExtensionColors = staticCompositionLocalOf {
    ExtensionColors.LightExtensionColors as ExtensionColors
}

val MaterialTheme.extensionColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtensionColors.current
