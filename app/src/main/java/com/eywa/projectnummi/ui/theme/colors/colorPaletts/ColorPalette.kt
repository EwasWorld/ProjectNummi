package com.eywa.projectnummi.ui.theme.colors.colorPaletts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.ui.theme.colors.ThemeColor

@Immutable
data class ColorPalette(
        val unassignedColor: Color = Color.Transparent,

        val appBackground: ThemeColor = ThemeColor(main = Color.White, on = Color.Black),
)