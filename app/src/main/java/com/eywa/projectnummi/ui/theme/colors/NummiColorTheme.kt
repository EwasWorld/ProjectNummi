package com.eywa.projectnummi.ui.theme.colors

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette

/**
 * Colour themes the user can switch between
 */
enum class NummiColorTheme(
        val colors: ColorPalette
) {
    LIGHT(
            colors = ColorPalette()
    ),
    DARK(
            colors = ColorPalette(
                    appBackground = ThemeColor(main = Color.Black, on = Color.White)
            )
    )
}