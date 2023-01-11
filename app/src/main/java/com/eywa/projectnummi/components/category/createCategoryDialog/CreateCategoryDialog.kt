package com.eywa.projectnummi.components.category.createCategoryDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.common.ColorUtils
import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun CreateCategoryDialog(
        isShown: Boolean,
        state: CreateCategoryDialogState?,
        listener: (CreateCategoryDialogIntent) -> Unit,
) {
    val isEditing = state?.editing != null
    NummiDialog(
            isShown = isShown && state != null,
            title = if (isEditing) "Editing " + state?.editing?.name else "New category",
            okButtonText = if (isEditing) "Update" else "Create",
            onOkListener = { listener(Submit) },
            onCancelListener = { listener(Close) },
    ) {
        NummiTextField(
                text = state?.name ?: "",
                onTextChanged = { listener(NameChanged(it)) },
                label = "Name",
                placeholderText = "Groceries",
        )
        Text(
                text = "Colour",
        )
        Column {
            Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(ColorUtils.asCategoryColor(state?.hue ?: 0.5f))
            )
            Slider(
                    value = state?.hue ?: 0.5f,
                    onValueChange = { listener(HueChanged(it)) },
                    colors = NummiTheme.colors.generalSlider(),
            )
        }
    }
}

@Preview(
        heightDp = 500,
        widthDp = 400,
)
@Composable
fun CreateCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateCategoryDialog(
                isShown = true,
                state = CreateCategoryDialogState(),
                listener = {},
        )
    }
}

@Preview(
        heightDp = 500,
        widthDp = 400,
)
@Composable
fun Edit_CreateCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateCategoryDialog(
                isShown = true,
                state = CreateCategoryDialogState(
                        editing = CategoryProvider.basic[1],
                ),
                listener = {},
        )
    }
}
