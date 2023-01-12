package com.eywa.projectnummi.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class NummiShapes(
        val generalButton: Shape = RoundedCornerShape(100),
        val generalListItem: Shape = RoundedCornerShape(20.dp),
)
