package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Category

object CategoryProvider {
    val basic = listOf(
            "Groceries",
            "Entertainment",
            "Work",
            "Income",
    ).mapIndexed { index, name -> Category(index, name) }
}