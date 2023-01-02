package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialogIntent
import java.util.*

sealed class AddTransactionsIntent {
    object CreateTransaction : AddTransactionsIntent()

    sealed class ValueChangedIntent : AddTransactionsIntent()
    object ToggleIsOutgoing : ValueChangedIntent()
    data class DateChanged(val date: Calendar) : ValueChangedIntent()
    data class DateIncremented(val daysAdded: Int) : ValueChangedIntent()
    data class AmountChanged(val amount: String) : ValueChangedIntent()
    data class NameChanged(val name: String) : ValueChangedIntent()

    object StartChangeCategory : AddTransactionsIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : AddTransactionsIntent()
    data class SelectCategoryDialogAction(val action: SelectCategoryDialogIntent) : AddTransactionsIntent()
}
