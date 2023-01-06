package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.common.DateUtils
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.model.Amount
import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.model.Transaction
import java.util.*
import kotlin.math.roundToInt

data class AddTransactionsState(
        val date: Calendar = DateUtils.currentDate(),
        val name: String = "",
        val isOutgoing: Boolean = true,

        /**
         * null for not loaded
         */
        val categories: List<Category>? = null,
        val createCategoryDialogState: CreateCategoryDialogState? = null,
        val selectCategoryDialogIsShown: Boolean = false,

        /**
         * null for not loaded
         */
        val people: List<Person>? = null,
        val createPersonDialogState: CreatePersonDialogState? = null,
        val selectPersonDialogIsShown: SelectPersonDialogPurpose? = null,

        val amountRows: List<AmountInputState> = listOf(AmountInputState()),
        val currentRow: Int? = null,
) {
    init {
        require(amountRows.isNotEmpty()) { "Must have at least one amount row" }
    }

    val totalAmount = amountRows.sumOf {
        try {
            it.amount.asPennyValue()
        }
        catch (e: NumberFormatException) {
            0
        }
    }

    fun getCategory(categoryId: Int?) = categoryId?.let { id -> categories?.find { it.id == id } }
    fun getPerson(personId: Int?) = personId?.let { id -> people?.find { it.id == id } }

    private fun String.asPennyValue() = (this.toDouble() * 100).roundToInt()

    fun asTransaction() = Transaction(
            id = 0,
            date = date,
            name = name,
            amount = amountRows.map {
                // TODO ERROR_HANDLING Prevent same category/person combination appearing more than once
                Amount(
                        amount = it.amount.asPennyValue(),
                        category = getCategory(it.categoryId),
                        person = getPerson(it.personId),
                )
            },
            isOutgoing = isOutgoing,
    )
}

data class AmountInputState(
        val amount: String = "",
        val categoryId: Int? = null,
        val personId: Int? = null,
)

enum class SelectPersonDialogPurpose {
    /**
     * Selecting a person for an [Amount]
     */
    SELECT,

    /**
     * Selecting a person to split the current [Amount] with
     */
    SPLIT,
}
