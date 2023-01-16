package com.eywa.projectnummi.sharedUi.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.sharedUi.CornerTriangleBox
import com.eywa.projectnummi.sharedUi.CornerTriangleShapeState
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun CategoryItem(
        category: Category?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
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

@Composable
fun CategoryItem(
        categories: List<Category?>,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
) {
    Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
    ) {
        CornerTriangleBox(
                colors = categories.map { it?.color ?: Color.Transparent },
                state = CornerTriangleShapeState(
                        isTop = false,
                        xScale = 2f,
                        yScale = if (categories.size > 1) 1.5f else 2f,
                ),
        )
        Text(
                text = when (categories.size) {
                    0 -> "All categories"
                    1 -> categories.first()?.name ?: "No category"
                    else -> "Various categories"
                },
                color = NummiTheme.colors.appBackground.content,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding)
        )
    }
}
