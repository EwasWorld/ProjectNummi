package com.eywa.projectnummi.features.addTransactions.selectCategoryDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialogIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.CornerTriangleShape
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.BaseColor

@Composable
fun SelectCategoryDialog(
        isShown: Boolean,
        state: SelectCategoryDialogState?,
        listener: (SelectCategoryDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Select a category",
            onCancelListener = { listener(Close) },
    ) {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            item {
                CategoryRow(text = "No category", color = null) { listener(NoCategoryClicked) }
            }

            items(state?.categories?.sortedBy { it.name } ?: listOf()) { item ->
                CategoryRow(text = item.name, color = item.color) { listener(CategoryClicked(item)) }
            }

            item {
                Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .clickable { listener(CreateNew) }
                ) {
                    Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = NummiTheme.colors.appBackground.content,
                    )
                    Text(
                            text = "New category",
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryRow(
        text: String,
        color: Color?,
        onClick: () -> Unit,
) {
    Box {
        Surface(
                color = Color.Transparent,
                border = BorderStroke(1.dp, BaseColor.GREY_500),
                shape = NummiTheme.shapes.generalListItem,
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                    modifier = Modifier
                            .matchParentSize()
                            .clip(CornerTriangleShape(isTop = false, xScale = 2f, yScale = 2f))
                            .background(color ?: Color.Transparent)
            )
            Text(
                    text = text,
                    color = NummiTheme.colors.appBackground.content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
            )
        }
    }
}

@Preview
@Composable
fun SelectCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectCategoryDialog(
                isShown = true,
                state = SelectCategoryDialogState(CategoryProvider.basic),
                listener = {},
        )
    }
}
