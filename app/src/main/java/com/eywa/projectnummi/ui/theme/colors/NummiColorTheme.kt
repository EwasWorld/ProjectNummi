package com.eywa.projectnummi.ui.theme.colors

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.ui.theme.colors.colorPaletts.ColorPalette

/**
 * Colour themes the user can switch between
 */
enum class NummiColorTheme(
        val colors: ColorPalette
) {
    MAIN(
            colors = ColorPalette()
    ),
    SECONDARY(
            colors = ColorPalette(
                    appBackground = ThemeColor(main = Color.White, content = Color.Black)
            )
    )
}