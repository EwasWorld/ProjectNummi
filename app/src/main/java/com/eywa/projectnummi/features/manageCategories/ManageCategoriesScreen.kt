package com.eywa.projectnummi.features.manageCategories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.AddCategoryClicked
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.CreateCategoryDialogAction
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.CornerTriangleShape
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.BaseColor

@Composable
fun ManageCategoriesScreen(
        viewModel: ManageCategoriesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state.collectAsState()
    ManageCategoriesScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageCategoriesScreen(
        state: ManageCategoriesState,
        listener: (ManageCategoriesIntent) -> Unit,
) {
    val displayItems = state.categories.sortedBy { it.name }

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
                Box {
                    Surface(
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, BaseColor.GREY_500),
                            shape = NummiTheme.shapes.generalListItem,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                                modifier = Modifier
                                        .matchParentSize()
                                        .clip(CornerTriangleShape(isTop = false, xScale = 2f, yScale = 2f))
                                        .background(item.color)
                        )
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
                        ) {
                            Text(
                                    text = item.name,
                                    color = NummiTheme.colors.appBackground.content,
                            )
                        }
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
