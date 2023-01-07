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
                    amount = listOf(
                            Amount(
                                    0,
                                    CategoryProvider.basic[1],
                                    null,
                                    1_50,
                            ),
                    ),
            ),
            Transaction(
                    id = 1,
                    date = DateProvider.getDate(-1),
                    name = "Gym",
                    amount = listOf(
                            Amount(
                                    1,
                                    null,
                                    PeopleProvider.basic[1],
                                    30,
                            ),
                    ),
                    account = AccountProvider.basic[1],
            ),
            Transaction(
                    id = 2,
                    date = DateProvider.getDate(-2),
                    name = "Salary",
                    amount = listOf(
                            Amount(
                                    2,
                                    CategoryProvider.basic[2],
                                    null,
                                    1000_00,
                            ),
                    ),
                    isOutgoing = false,
                    account = AccountProvider.basic[2],
            ),
            Transaction(
                    id = 3,
                    date = DateProvider.getDate(-3),
                    name = "Amazon",
                    amount = listOf(
                            Amount(
                                    3,
                                    CategoryProvider.basic[0],
                                    PeopleProvider.basic[0],
                                    13_59,
                            ),
                            Amount(
                                    4,
                                    CategoryProvider.basic[3],
                                    null,
                                    29_99,
                            ),
                    ),
                    order = 1,
            ),
            Transaction(
                    id = 4,
                    date = DateProvider.getDate(-3),
                    name = "Toaster",
                    amount = listOf(
                            Amount(
                                    5,
                                    null,
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 2,
                    account = AccountProvider.basic[0],
            ),
    )
}
