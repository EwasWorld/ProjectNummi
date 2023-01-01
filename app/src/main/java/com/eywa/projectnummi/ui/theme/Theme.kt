package com.eywa.projectnummi.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.eywa.projectnummi.ui.theme.colors.NummiColorTheme
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette


val LocalDimens = staticCompositionLocalOf { Dimens() }

val LocalColorPalette = staticCompositionLocalOf { ColorPalette() }
var currentColorTheme by mutableStateOf(NummiColorTheme.MAIN)


@Composable
fun NummiTheme(
        colorTheme: NummiColorTheme = currentColorTheme,
        dimens: Dimens = Dimens(),
        content: @Composable () -> Unit
) {
    CompositionLocalProvider(
            LocalColorPalette provides colorTheme.colors,
            LocalDimens provides dimens,
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
    val dimens: Dimens
        @Composable
        get() = LocalDimens.current
}
