package com.eywa.projectnummi.theme

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
        val listItemBorder: Dp = 1.dp,
        val pillBorder: Dp = 1.dp,
        val listItemPadding: Dp = 15.dp,
        val listItemSpacedBy: Dp = 10.dp,
        val viewTransactionTriangleSize: Dp = 130.dp,

        val buttonElevationNone: @Composable () -> ButtonElevation =
                { ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp) },
)
