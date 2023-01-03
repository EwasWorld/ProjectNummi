package com.eywa.projectnummi.features.manageCategories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.AddCategoryClicked
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.CreateCategoryDialogAction
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.CornerTriangleBox
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun ManageCategoriesScreen(
        viewModel: ManageCategoriesViewModel = viewModel(),
) {
    val state = viewModel.state.collectAsState()
    ManageCategoriesScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageCategoriesScreen(
        state: ManageCategoriesState,
        listener: (ManageCategoriesIntent) -> Unit,
) {
    val displayItems = state.categories?.sortedBy { it.name } ?: listOf()

    CreateCategoryDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreateCategoryDialogAction(it)) },
    )

    Box {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
                modifier = Modifier.fillMaxSize()
        ) {
            items(displayItems) { item ->
                Surface(
                        color = Color.Transparent,
                        border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
                        shape = NummiTheme.shapes.generalListItem,
                        modifier = Modifier.fillMaxWidth()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CornerTriangleBox(
                                color = item.color,
                                isTop = false,
                                xScale = 2f,
                                yScale = 2f,
                        )
                        Text(
                                text = item.name,
                                color = NummiTheme.colors.appBackground.content,
                                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
                        )
                    }
                }
            }
        }

        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { listener(AddCategoryClicked) },
                modifier = Modifier
                        .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                        .align(Alignment.BottomEnd)
        ) {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add category",
            )
        }
    }
}

@Preview
@Composable
fun ManageCategoriesScreen_Preview() {
    NummiScreenPreviewWrapper {
        ManageCategoriesScreen(
                state = ManageCategoriesState(
                        categories = CategoryProvider.basic,
                )
        ) {}
    }
}
