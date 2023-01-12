package com.eywa.projectnummi.sharedUi.category

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
import androidx.compose.ui.text.style.TextAlign
import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.sharedUi.CornerTriangleBox
import com.eywa.projectnummi.sharedUi.CornerTriangleShapeState
import com.eywa.projectnummi.theme.NummiTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(
        category: Category?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
        ) {
            CornerTriangleBox(
                    color = category?.color ?: Color.Transparent,
                    state = CornerTriangleShapeState(
                            isTop = false,
                            xScale = 2f,
                            yScale = 2f,
                    ),
            )
            Text(
                    text = category?.name ?: "No category",
                    color = NummiTheme.colors.appBackground.content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(contentPadding)
            )
        }
    }
}
