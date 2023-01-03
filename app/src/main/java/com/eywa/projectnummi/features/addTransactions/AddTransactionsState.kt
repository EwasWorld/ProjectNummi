package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.model.Amount
import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.model.Transaction
import com.eywa.projectnummi.ui.utils.DateUtils
import java.util.*
import kotlin.math.roundToInt

data class AddTransactionsState(
        val date: Calendar = DateUtils.currentDate(),
        val amount: String = "",
        val name: String = "",
        val isOutgoing: Boolean = true,

        val categoryId: Int? = null,
        /**
         * null for not loaded
         */
        val categories: List<Category>? = null,
        val createCategoryDialogState: CreateCategoryDialogState? = null,
        val selectCategoryDialogIsShown: Boolean = false,

        val personId: Int? = null,
        /**
         * null for not loaded
         */
        val people: List<Person>? = null,
        val createPersonDialogState: CreatePersonDialogState? = null,
        val selectPersonDialogIsShown: Boolean = false,
) {
    val category = categoryId?.let { id -> categories?.find { it.id == id } }
    val person = personId?.let { id -> people?.find { it.id == id } }

    fun asTransaction() = Transaction(
            id = 0,
            date = date,
            name = name,
            amount = listOf(
                    Amount(
                            amount = (amount.toDouble() * 100).roundToInt(),
                            category = category,
                            person = person!!,
                    ),
            ),
            isOutgoing = isOutgoing,
    )
}
