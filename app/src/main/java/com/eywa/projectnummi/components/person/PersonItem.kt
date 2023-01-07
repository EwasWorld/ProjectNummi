package com.eywa.projectnummi.components.person

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.ui.theme.NummiTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonItem(
        person: Person?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
            modifier = modifier
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                    text = person?.name ?: "Default",
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}
