package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.model.*
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.utils.DateUtils
import com.eywa.projectnummi.utils.div100String
import java.util.*
import kotlin.math.roundToInt

data class AddTransactionsState(
        val editing: Transaction? = null,
        val isEditComplete: Boolean = false,

        val date: Calendar = editing?.date ?: DateUtils.currentDate(),
        val name: String = editing?.name ?: "",
        val isOutgoing: Boolean = editing?.isOutgoing ?: true,

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

        /**
         * null for not loaded
         */
        val accounts: List<Account>? = null,
        val accountId: Int? = editing?.account?.id,
        val createAccountDialogState: CreateAccountDialogState? = null,
        val selectAccountDialogIsShown: Boolean = false,

        val amountRows: List<AmountInputState> =
                editing?.amount?.map { AmountInputState(it) } ?: listOf(AmountInputState()),
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

    val account = accountId?.let { accounts?.find { it.id == accountId } }
    val isEditing = editing != null

    fun getCategory(categoryId: Int?) = categoryId?.let { id -> categories?.find { it.id == id } }
    fun getPerson(personId: Int?) = personId?.let { id -> people?.find { it.id == id } }

    private fun String.asPennyValue() = (this.toDouble() * 100).roundToInt()

    fun asTransaction() = Transaction(
            id = editing?.id ?: 0,
            date = date,
            name = name,
            amount = amountRows.mapNotNull { amountState ->
                // TODO ERROR_HANDLING Prevent same category/person combination appearing more than once
                if (amountState.amount.isBlank()) return@mapNotNull null
                val oldAmount = editing?.amount
                        ?.find { it.category?.id == amountState.categoryId && it.person?.id == amountState.personId }
                Amount(
                        id = oldAmount?.id ?: 0,
                        amount = amountState.amount.asPennyValue(),
                        category = getCategory(amountState.categoryId),
                        person = getPerson(amountState.personId),
                )
            },
            isOutgoing = isOutgoing,
            account = account,
            order = editing?.order ?: -1,
    )
}

data class AmountInputState(
        val amount: String = "",
        val categoryId: Int? = null,
        val personId: Int? = null,
) {
    constructor(amount: Amount) : this(amount.amount.div100String(), amount.category?.id, amount.person?.id)
}

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
