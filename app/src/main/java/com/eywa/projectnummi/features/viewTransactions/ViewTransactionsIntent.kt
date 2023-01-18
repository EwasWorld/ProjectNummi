package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ViewTransactionsIntent {
    data class ClearExtra(val extra: ViewTransactionsExtra) : ViewTransactionsIntent()
    data class TransactionClicked(val transaction: Transaction) : ViewTransactionsIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ViewTransactionsIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ViewTransactionsIntent()
    data class TabClicked(val item: ManageTabSwitcherItem) : ViewTransactionsIntent()
    object AddClicked : ViewTransactionsIntent()
}
