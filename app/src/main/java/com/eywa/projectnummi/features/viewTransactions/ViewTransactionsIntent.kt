package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent

sealed class ViewTransactionsIntent {
    object NavigatedToEditItem : ViewTransactionsIntent()
    data class TransactionClicked(val transaction: Transaction) : ViewTransactionsIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ViewTransactionsIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ViewTransactionsIntent()
}
