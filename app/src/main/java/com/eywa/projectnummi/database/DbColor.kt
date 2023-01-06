package com.eywa.projectnummi.database

import androidx.compose.ui.graphics.Color

/**
 * For some reason Room isn't picking up [DatabaseConverters.toColor] and [DatabaseConverters.fromColor]
 * properly when the type is [Color]. Wrapper enforces the correct behaviour
 */
data class DbColor(val value: Color)
