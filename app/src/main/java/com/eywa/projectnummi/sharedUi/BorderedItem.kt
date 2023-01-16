package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.theme.NummiTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BorderedItem(
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
            modifier = modifier,
            content = content
    )
}
