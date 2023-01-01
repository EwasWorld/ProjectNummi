package com.eywa.projectnummi.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.eywa.projectnummi.ui.theme.colors.NummiColorTheme
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette


val LocalColorPalette = staticCompositionLocalOf { ColorPalette() }
var currentAppTheme by mutableStateOf(NummiColorTheme.LIGHT)


@Composable
fun NummiTheme(
        theme: NummiColorTheme = currentAppTheme,
        content: @Composable () -> Unit
) {
    CompositionLocalProvider(
            LocalColorPalette provides theme.colors,
    ) {
        MaterialTheme(
                typography = Typography,
                shapes = Shapes,
                content = content
        )
    }
}

object NummiTheme {
    val colors: ColorPalette
        @Composable
        get() = LocalColorPalette.current
}
