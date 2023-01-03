package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.common.providers.DateProvider
import com.eywa.projectnummi.model.Transaction

object TransactionProvider {
    val basic = listOf(
            Transaction(0,
                    DateProvider.getDate(0),
                    "Sainsburys",
                    1_50,
                    CategoryProvider.basic[1],
                    PeopleProvider.basic[0]),
            Transaction(1, DateProvider.getDate(-1), "Gym", 30, null, PeopleProvider.basic[2]),
            Transaction(2,
                    DateProvider.getDate(-2),
                    "Salary",
                    1000_00,
                    CategoryProvider.basic[2],
                    PeopleProvider.basic[0],
                    false),
            Transaction(3,
                    DateProvider.getDate(-3),
                    "Amazon",
                    13_59,
                    CategoryProvider.basic[0],
                    PeopleProvider.basic[1]),
    )
}
