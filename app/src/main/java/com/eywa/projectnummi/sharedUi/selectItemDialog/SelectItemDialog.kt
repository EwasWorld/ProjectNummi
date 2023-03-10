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
import com.eywa.projectnummi.utils.CategorySortNode


@Composable
fun <I : HasNameAndId> SelectItemDialog(
        title: String,
        keepOrder: Boolean = false,
        newItemButtonText: String? = null,
        isShown: Boolean = true,
        hasDefaultItem: Boolean = true,
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
                keepOrder = keepOrder,
                onNewItemClicked = { listener(CreateNew) },
                hasDefaultItem = hasDefaultItem,
                newItemButtonText = newItemButtonText?.takeIf { !isMultiSelect },
        ) { item ->
            BorderedItem(
                    isSelected = state?.isSelected(item) ?: false,
                    onSelected = { listener(if (isMultiSelect) ToggleItemSelected(item) else ItemChosen(item)) },
                    modifier = Modifier.fillMaxWidth()
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun SelectAccountDialog(
        isShown: Boolean = true,
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
        isShown: Boolean = true,
        showCreateNew: Boolean = true,
        state: SelectItemDialogState<Category>?,
        listener: (SelectItemDialogIntent) -> Unit,
) = SelectItemDialog(
        isShown = isShown,
        state = state?.copy(items = CategorySortNode.generate(state.items).getOrdered()),
        keepOrder = true,
        listener = listener,
        itemContent = { CategoryItem(category = it) },
        title = if (state?.allowMultiSelect == true) "Select categories" else "Select a category",
        newItemButtonText = "New category".takeIf { showCreateNew },
)

@Composable
fun SelectPersonDialog(
        isShown: Boolean = true,
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
