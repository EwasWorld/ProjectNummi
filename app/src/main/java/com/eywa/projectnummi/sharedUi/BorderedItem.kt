package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.selection.selectable
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
        isSelected: Boolean,
        onSelected: (() -> Unit),
        content: @Composable () -> Unit,
) {
    Surface(
            color = if (isSelected) NummiTheme.colors.listItemSelectedBackground else Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.getBorder(isSelected), NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onSelected,
            content = content,
            modifier = modifier.selectable(selected = isSelected, onClick = onSelected),
    )
}

@Composable
fun BorderedItem(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.getBorder(false), NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            content = content,
            modifier = modifier,
    )
}
