package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.common.providers.DateProvider
import com.eywa.projectnummi.model.Amount
import com.eywa.projectnummi.model.Transaction

object TransactionProvider {
    val basic = listOf(
            Transaction(
                    id = 0,
                    date = DateProvider.getDate(0),
                    name = "Sainsburys",
                    amount = Amount(
                            CategoryProvider.basic[1],
                            PeopleProvider.basic[0],
                            1_50,
                    ),
            ),
            Transaction(
                    id = 1,
                    date = DateProvider.getDate(-1),
                    name = "Gym",
                    amount = Amount(
                            null,
                            PeopleProvider.basic[2],
                            30,
                    ),
            ),
            Transaction(
                    id = 2,
                    date = DateProvider.getDate(-2),
                    name = "Salary",
                    amount = Amount(
                            CategoryProvider.basic[2],
                            PeopleProvider.basic[0],
                            1000_00,
                    ),
                    isOutgoing = false
            ),
            Transaction(
                    id = 3,
                    date = DateProvider.getDate(-3),
                    name = "Amazon",
                    amount = Amount(
                            CategoryProvider.basic[0],
                            PeopleProvider.basic[1],
                            13_59,
                    ),
            ),
    )
}
