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
        isSelected: Boolean? = null,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit,
) {
    val selectable = if (isSelected == null) Modifier else Modifier.selectable(isSelected, onClick = onClick)
    Surface(
            color = if (isSelected == true) NummiTheme.colors.listItemSelectedBackground else Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.getBorder(isSelected ?: false), NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
            content = content,
            modifier = modifier.then(selectable),
    )
}
