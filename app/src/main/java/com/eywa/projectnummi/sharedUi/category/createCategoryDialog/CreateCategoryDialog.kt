package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.sharedUi.*
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogIntent.*
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectCategoryDialog
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogState
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.ColorUtils

@Composable
fun CreateCategoryDialog(
        isShown: Boolean,
        state: CreateCategoryDialogState?,
        listener: (CreateCategoryDialogIntent) -> Unit,
) {
    SelectCategoryDialog(
            isShown = state?.selectParentCategoryDialogOpen ?: false,
            state = SelectItemDialogState(state?.filteredCategories ?: emptyList()),
            listener = { listener(SelectParentCategoryDialogAction(it)) },
            showCreateNew = false,
    )
    if (state?.selectParentCategoryDialogOpen == true) return

    val isEditing = state?.editing != null
    NummiDialog(
            isShown = isShown && state != null,
            title = if (isEditing) "Editing " + state?.editing?.name else "New category",
            okButtonText = if (isEditing) "Update" else "Create",
            onOkListener = { listener(Submit) },
            onCancelListener = { listener(Close) },
    ) {
        Column {
            NummiTextField(
                    text = state?.name ?: "",
                    onTextChanged = { listener(NameChanged(it)) },
                    label = "Name",
                    placeholderText = "Groceries",
            )

            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                            .padding(top = 25.dp, bottom = 10.dp)
                            .fillMaxWidth()
            ) {
                Text(
                        text = "Parent",
                        style = NummiTheme.typography.h6,
                )
                BorderedItem(
                        onClick = { listener(OpenSelectParentCategoryDialog) },
                        content = { CategoryItem(category = state?.parentCategory) },
                )
            }

            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                            .padding(bottom = 10.dp)
                            .fillMaxWidth()
            ) {
                Text(
                        text = "Colour",
                        style = NummiTheme.typography.h6,
                )
                CheckboxInput(
                        text = "Match parent",
                        isSelected = state?.matchParentColor ?: false,
                        onClick = { listener(ToggleMatchParentColor) },
                )
            }
            Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                    state?.parentCategory?.displayColor?.takeIf { state.matchParentColor }
                                            ?: ColorUtils.asCategoryColor(state?.hue ?: 0.5f)
                            )
            )
            Slider(
                    value = state?.hue ?: 0.5f,
                    onValueChange = { listener(HueChanged(it)) },
                    colors = NummiTheme.colors.generalSlider(),
                    enabled = state?.matchParentColor != true,
            )
        }
    }
}

@Preview(
        heightDp = 700,
        widthDp = 400,
)
@Composable
fun CreateCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateCategoryDialog(
                isShown = true,
                state = CreateCategoryDialogState(categories = null),
                listener = {},
        )
    }
}

@Preview(
        heightDp = 700,
        widthDp = 400,
)
@Composable
fun Edit_CreateCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateCategoryDialog(
                isShown = true,
                state = CreateCategoryDialogState(
                        editing = CategoryProvider.basic[1],
                        categories = null,
                ),
                listener = {},
        )
    }
}
