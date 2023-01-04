package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.components.selectCategoryDialog.SelectCategoryDialogIntent
import com.eywa.projectnummi.components.selectPersonDialog.SelectPersonDialogIntent
import java.util.*

sealed class AddTransactionsIntent {
    object Submit : AddTransactionsIntent()
    object Clear : AddTransactionsIntent()
    object Split : AddTransactionsIntent()
    object AddAmountRow : AddTransactionsIntent()

    sealed class ValueChangedIntent : AddTransactionsIntent()
    object ToggleIsOutgoing : ValueChangedIntent()
    data class DateChanged(val date: Calendar) : ValueChangedIntent()
    data class DateIncremented(val daysAdded: Int) : ValueChangedIntent()
    data class NameChanged(val name: String) : ValueChangedIntent()
    data class AmountChanged(val rowIndex: Int, val amount: String) : ValueChangedIntent()

    data class DeleteAmountRow(val rowIndex: Int) : AddTransactionsIntent()

    data class StartChangeCategory(val rowIndex: Int) : AddTransactionsIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : AddTransactionsIntent()
    data class SelectCategoryDialogAction(val action: SelectCategoryDialogIntent) : AddTransactionsIntent()

    data class StartChangePerson(val rowIndex: Int) : AddTransactionsIntent()
    data class CreatePersonDialogAction(val action: CreatePersonDialogIntent) : AddTransactionsIntent()
    data class SelectPersonDialogAction(val action: SelectPersonDialogIntent) : AddTransactionsIntent()
}
