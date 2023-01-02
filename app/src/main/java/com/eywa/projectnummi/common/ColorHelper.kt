package com.eywa.projectnummi.common

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi

object ColorHelper {
    private const val MAX_HUE_VALUE = 360f

    @OptIn(ExperimentalGraphicsApi::class)
    fun asCategoryColor(@FloatRange(from = 0.0, to = 1.0) huePercentage: Float) =
            Color.hsv(huePercentage * MAX_HUE_VALUE, 0.7f, 0.7f)
}