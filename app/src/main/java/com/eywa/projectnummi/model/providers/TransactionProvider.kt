package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.objects.Amount
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.utils.providers.DateProvider

object TransactionProvider {
    val basic = listOf(
            Transaction(
                    id = 1,
                    date = DateProvider.getDate(0),
                    name = "Sainsburys but a really long name",
                    amounts = listOf(
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
                    amounts = listOf(
                            Amount(
                                    2,
                                    CategoryProvider.basic[7],
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
                    amounts = listOf(
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
                    amounts = listOf(
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
                    amounts = listOf(
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
                    amounts = listOf(
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
                    amounts = listOf(
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
                    amounts = listOf(
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
                    amounts = listOf(
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
            Transaction(
                    id = 10,
                    date = DateProvider.getDate(-44),
                    name = "Water bill",
                    amounts = listOf(
                            Amount(
                                    11,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 3,
                    account = AccountProvider.basic[1],
                    isRecurring = true,
            ),
            Transaction(
                    id = 11,
                    date = DateProvider.getDate(-44),
                    name = "Electric bill",
                    amounts = listOf(
                            Amount(
                                    12,
                                    CategoryProvider.basic[4],
                                    null,
                                    20_00,
                            ),
                    ),
                    order = 4,
                    account = AccountProvider.basic[1],
                    isRecurring = true,
            ),
            Transaction(
                    id = 12,
                    date = DateProvider.getDate(-1),
                    name = "No cat",
                    amounts = listOf(
                            Amount(
                                    13,
                                    null,
                                    PeopleProvider.basic[1],
                                    30,
                            ),
                    ),
                    account = AccountProvider.basic[1],
            ),
    )
}
