package com.eywa.projectnummi.utils

import android.graphics.Color.colorToHSV
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.toArgb

object ColorUtils {
    private const val MAX_HUE_VALUE = 360f

    @OptIn(ExperimentalGraphicsApi::class)
    fun asCategoryColor(@FloatRange(from = 0.0, to = 1.0) huePercentage: Float) =
            Color.hsv(huePercentage * MAX_HUE_VALUE, 0.7f, 0.7f)

    fun colorToHuePercentage(color: Color): Float {
        val hsv = FloatArray(3)
        colorToHSV(color.toArgb(), hsv)
        return hsv[0] / MAX_HUE_VALUE
    }
}
