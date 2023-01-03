package com.eywa.projectnummi.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import com.eywa.projectnummi.ui.theme.colors.NummiColorTheme
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette


val LocalDimens = staticCompositionLocalOf { NummiDimens() }
val LocalShapes = staticCompositionLocalOf { NummiShapes() }
val LocalTypography = staticCompositionLocalOf { NummiTypography }

val LocalColorPalette = staticCompositionLocalOf { ColorPalette() }
var currentColorTheme by mutableStateOf(NummiColorTheme.MAIN)


@Composable
fun NummiTheme(
        colorTheme: NummiColorTheme = currentColorTheme,
        dimens: NummiDimens = NummiDimens(),
        shapes: NummiShapes = NummiShapes(),
        typography: Typography = NummiTypography,
        content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
            LocalColorPalette provides colorTheme.colors,
            LocalDimens provides dimens,
            LocalShapes provides shapes,
            LocalTypography provides typography,
    ) {
        MaterialTheme(
                typography = LocalTypography.current,
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
    val typography: Typography
        @Composable
        get() = LocalTypography.current
}
