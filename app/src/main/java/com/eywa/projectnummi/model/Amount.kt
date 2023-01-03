package com.eywa.projectnummi.model

data class Amount(
        val category: Category?,
        val person: Person,
        val amount: Int,
) {
    fun categoryPersonString(defaultPersonId: Int) =
            listOfNotNull(
                    category?.name,
                    person.takeIf { it.id != defaultPersonId }?.name,
            ).joinToString(" - ")
}
