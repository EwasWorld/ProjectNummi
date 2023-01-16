package com.eywa.projectnummi.sharedUi.selectItemDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.HasNameAndId
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.sharedUi.BorderedItem
import com.eywa.projectnummi.sharedUi.ItemList
import com.eywa.projectnummi.sharedUi.NummiDialog
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent.*


@Composable
fun <I : HasNameAndId> SelectItemDialog(
        title: String,
        newItemButtonText: String,
        isShown: Boolean,
        state: SelectItemDialogState<I>?,
        listener: (SelectItemDialogIntent) -> Unit,
        itemContent: @Composable (item: I?) -> Unit,
) {
    val isMultiSelect = state?.allowMultiSelect == true

    NummiDialog(
            isShown = isShown && state != null,
            okButtonText = "Select".takeIf { isMultiSelect },
            onOkListener = { listener(Submit) },
            title = title,
            onCancelListener = { listener(Close) },
    ) {
        ItemList(
                items = state?.items,
                onNewItemClicked = { listener(CreateNew) },
                hasDefaultItem = true,
                newItemButtonText = newItemButtonText.takeIf { !isMultiSelect },
        ) { item ->
            BorderedItem(
                    isSelected = state?.isSelected(item),
                    onClick = { listener(if (isMultiSelect) ToggleItemSelected(item) else ItemChosen(item)) },
                    modifier = Modifier.fillMaxWidth()
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun SelectAccountDialog(
        isShown: Boolean,
        state: SelectItemDialogState<Account>?,
        listener: (SelectItemDialogIntent) -> Unit,
) = SelectItemDialog(
        isShown = isShown,
        state = state,
        listener = listener,
        itemContent = { AccountItem(account = it) },
        title = if (state?.allowMultiSelect == true) "Select accounts" else "Select an account",
        newItemButtonText = "New account",
)

@Composable
fun SelectCategoryDialog(
        isShown: Boolean,
        state: SelectItemDialogState<Category>?,
        listener: (SelectItemDialogIntent) -> Unit,
) = SelectItemDialog(
        isShown = isShown,
        state = state,
        listener = listener,
        itemContent = { CategoryItem(category = it) },
        title = if (state?.allowMultiSelect == true) "Select categories" else "Select a category",
        newItemButtonText = "New category",
)

@Composable
fun SelectPersonDialog(
        isShown: Boolean,
        state: SelectItemDialogState<Person>?,
        listener: (SelectItemDialogIntent) -> Unit,
) = SelectItemDialog(
        isShown = isShown,
        state = state,
        listener = listener,
        itemContent = { PersonItem(person = it) },
        title = if (state?.allowMultiSelect == true) "Select people" else "Select a person",
        newItemButtonText = "New person",
)

@Preview
@Composable
fun SelectItemDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectAccountDialog(
                isShown = true,
                state = SelectItemDialogState(AccountProvider.basic),
                listener = {},
        )
    }
}

@Preview
@Composable
fun Multi_SelectItemDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectAccountDialog(
                isShown = true,
                state = SelectItemDialogState(AccountProvider.basic, listOf(1), true),
                listener = {},
        )
    }
}
