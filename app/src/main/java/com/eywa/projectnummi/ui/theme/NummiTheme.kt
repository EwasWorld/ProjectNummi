package com.eywa.projectnummi.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.eywa.projectnummi.ui.theme.colors.NummiColorTheme
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette


val LocalDimens = staticCompositionLocalOf { NummiDimens() }
val LocalShapes = staticCompositionLocalOf { NummiShapes() }

val LocalColorPalette = staticCompositionLocalOf { ColorPalette() }
var currentColorTheme by mutableStateOf(NummiColorTheme.MAIN)


@Composable
fun NummiTheme(
        colorTheme: NummiColorTheme = currentColorTheme,
        dimens: NummiDimens = NummiDimens(),
        shapes: NummiShapes = NummiShapes(),
        content: @Composable () -> Unit
) {
    CompositionLocalProvider(
            LocalColorPalette provides colorTheme.colors,
            LocalDimens provides dimens,
            LocalShapes provides shapes,
    ) {
        MaterialTheme(
                typography = Typography,
                content = content
        )
    }
}

object NummiTheme {
    val colors: ColorPalette
        @Composable
        get() = LocalColorPalette.current
    val dimens: NummiDimens
        @Composable
        get() = LocalDimens.current
    val shapes: NummiShapes
        @Composable
        get() = LocalShapes.current
}
