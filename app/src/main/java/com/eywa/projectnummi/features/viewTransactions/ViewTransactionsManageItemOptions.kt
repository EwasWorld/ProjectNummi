package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDefaultOption
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemOption

sealed class ViewTransactionsManageItemOptions(
        override val text: String,
        val shouldShow: (state: ViewTransactionsState, selectedItem: Transaction) -> Boolean = { _, _ -> true },
) : ManageItemOption {
    object MoveUp : ViewTransactionsManageItemOptions(
            "Move up",
            { state, selectedItem ->
                val matchingDate = state.transactions.filter { it.date == selectedItem.date }
                matchingDate.size > 1 && selectedItem.order != matchingDate.maxOfOrNull { it.order }
            },
    )

    object MoveDown : ViewTransactionsManageItemOptions(
            "Move down",
            { state, selectedItem ->
                val matchingDate = state.transactions.filter { it.date == selectedItem.date }
                matchingDate.size > 1 && selectedItem.order != matchingDate.minOfOrNull { it.order }
            },
    )

    object Add : ViewTransactionsManageItemOptions("Add")
    object Delete : ViewTransactionsManageItemOptions(ManageItemDefaultOption.DELETE.text)
    object Edit : ViewTransactionsManageItemOptions(ManageItemDefaultOption.EDIT.text)
}
