package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent
import java.util.*

sealed class AddTransactionsIntent {
    object Submit : AddTransactionsIntent()
    object Clear : AddTransactionsIntent()
    object Split : AddTransactionsIntent()
    object AddAmountRow : AddTransactionsIntent()

    sealed class ValueChangedIntent : AddTransactionsIntent()
    object ToggleIsRecurring : ValueChangedIntent()
    object ToggleIsOutgoing : ValueChangedIntent()
    data class DateChanged(val date: Calendar) : ValueChangedIntent()
    data class DateIncremented(val daysAdded: Int) : ValueChangedIntent()
    data class NameChanged(val name: String) : ValueChangedIntent()
    data class AmountChanged(val rowIndex: Int, val amount: String) : ValueChangedIntent()

    data class DeleteAmountRow(val rowIndex: Int) : AddTransactionsIntent()

    data class StartChangeCategory(val rowIndex: Int) : AddTransactionsIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : AddTransactionsIntent()
    data class SelectCategoryDialogAction(val action: SelectItemDialogIntent) : AddTransactionsIntent()

    data class StartChangePerson(val rowIndex: Int) : AddTransactionsIntent()
    data class CreatePersonDialogAction(val action: CreatePersonDialogIntent) : AddTransactionsIntent()
    data class SelectPersonDialogAction(val action: SelectItemDialogIntent) : AddTransactionsIntent()

    object StartChangeAccount : AddTransactionsIntent()
    data class CreateAccountDialogAction(val action: CreateAccountDialogIntent) : AddTransactionsIntent()
    data class SelectAccountDialogAction(val action: SelectItemDialogIntent) : AddTransactionsIntent()

    object StartSelectTransaction : AddTransactionsIntent()
    data class SelectTransactionDialogAction(val action: SelectItemDialogIntent) : AddTransactionsIntent()
}
