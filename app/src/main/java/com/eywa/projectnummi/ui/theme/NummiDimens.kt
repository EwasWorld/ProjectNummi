package com.eywa.projectnummi.ui.theme

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class NummiDimens(
        val screenPadding: Dp = 20.dp,
        val fabToScreenEdgePadding: Dp = 30.dp,

        val buttonElevationNone: @Composable () -> ButtonElevation =
                { ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp) }
)