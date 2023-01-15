package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Amount
import com.eywa.projectnummi.model.Transaction
import com.eywa.projectnummi.utils.providers.DateProvider

object TransactionProvider {
    val basic = listOf(
            Transaction(
                    id = 1,
                    date = DateProvider.getDate(0),
                    name = "Sainsburys",
                    amount = listOf(
                            Amount(
                                    1,
                                    CategoryProvider.basic[1],
                                    null,
                                    1_50,
                            ),
                    ),
            ),
            Transaction(
                    id = 2,
                    date = DateProvider.getDate(-1),
                    name = "Gym",
                    amount = listOf(
                            Amount(
                                    2,
                                    null,
                                    PeopleProvider.basic[1],
                                    30,
                            ),
                    ),
                    account = AccountProvider.basic[1],
            ),
            Transaction(
                    id = 3,
                    date = DateProvider.getDate(-2),
                    name = "Salary",
                    amount = listOf(
                            Amount(
                                    3,
                                    CategoryProvider.basic[2],
                                    null,
                                    1000_00,
                            ),
                    ),
                    isOutgoing = false,
                    account = AccountProvider.basic[2],
            ),
            Transaction(
                    id = 4,
                    date = DateProvider.getDate(-3),
                    name = "Amazon",
                    amount = listOf(
                            Amount(
                                    4,
                                    CategoryProvider.basic[0],
                                    PeopleProvider.basic[0],
                                    13_59,
                            ),
                            Amount(
                                    5,
                                    CategoryProvider.basic[3],
                                    null,
                                    29_99,
                            ),
                    ),
                    order = 1,
            ),
            Transaction(
                    id = 5,
                    date = DateProvider.getDate(-3),
                    name = "Toaster",
                    amount = listOf(
                            Amount(
                                    6,
                                    null,
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 2,
                    account = AccountProvider.basic[0],
            ),
            Transaction(
                    id = 6,
                    date = DateProvider.getDate(-4),
                    name = "Water bill",
                    amount = listOf(
                            Amount(
                                    7,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 1,
                    account = AccountProvider.basic[1],
            ),
            Transaction(
                    id = 7,
                    date = DateProvider.getDate(-4),
                    name = "Electric bill",
                    amount = listOf(
                            Amount(
                                    8,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 2,
                    account = AccountProvider.basic[1],
            ),
            Transaction(
                    id = 8,
                    date = DateProvider.getDate(-44),
                    name = "Water bill",
                    amount = listOf(
                            Amount(
                                    9,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 1,
                    account = AccountProvider.basic[1],
            ),
            Transaction(
                    id = 9,
                    date = DateProvider.getDate(-44),
                    name = "Electric bill",
                    amount = listOf(
                            Amount(
                                    10,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 2,
                    account = AccountProvider.basic[1],
            ),
    )
}
