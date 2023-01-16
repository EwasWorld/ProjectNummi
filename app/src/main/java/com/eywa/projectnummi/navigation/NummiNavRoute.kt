package com.eywa.projectnummi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.eywa.projectnummi.features.addTransactions.AddTransactionsScreen
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsScreen
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesScreen
import com.eywa.projectnummi.features.managePeople.ManagePeopleScreen
import com.eywa.projectnummi.features.transactionsSummary.ui.TransactionsSummaryScreen
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsScreen

fun String.toNummiNavRoute() =
        try {
            NummiNavRoute.valueOf(uppercase())
        }
        catch (e: IllegalArgumentException) {
            null
        }

enum class NummiNavRoute : NavRoute {
    ADD_TRANSACTIONS {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                AddTransactionsScreen(navController)

        override fun saveStateOnNavigate(arguments: Map<String, NavArgument>?) = true
    },
    EDIT_TRANSACTIONS {
        override val arguments = listOf(
                navArgument(NummiNavArgument.TRANSACTION_ID.toArgName()) {
                    type = NavType.StringType
                    nullable = false
                }
        )

        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                AddTransactionsScreen(navController)
    },
    VIEW_TRANSACTIONS {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                ViewTransactionsScreen(navController)
    },
    TRANSACTIONS_SUMMARY {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                TransactionsSummaryScreen()

        // TODO Save state isn't working :(
        override fun saveStateOnNavigate(arguments: Map<String, NavArgument>?) = true
    },
    MANAGE_CATEGORIES {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                ManageCategoriesScreen(navController)
    },
    MANAGE_PEOPLE {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                ManagePeopleScreen(navController)
    },
    MANAGE_ACCOUNTS {
        @Composable
        override fun Screen(navController: NavController, entry: NavBackStackEntry) =
                ManageAccountsScreen(navController)
    },
    ;

    override val routeBase = "mainnavroute_" + name.lowercase()

    protected open val arguments: List<NamedNavArgument> = emptyList()

    open fun saveStateOnNavigate(arguments: Map<String, NavArgument>?): Boolean = false

    @Composable
    abstract fun Screen(navController: NavController, entry: NavBackStackEntry)

    open fun navigate(
            navController: NavController,
            args: Map<NummiNavArgument, String> = emptyMap(),
            options: (NavOptionsBuilder.() -> Unit)? = null,
    ) {
        check(arguments.map { it.name } == args.map { it.key.toArgName() }) { "Invalid arguments" }
        val route = routeBase + arguments.joinToString { "/" + args[it.name.toNummiNavArgument()!!]!! }

        if (options == null) {
            navController.navigate(route)
        }
        else {
            navController.navigate(route, options)
        }
    }

    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(
                route = routeBase + arguments.joinToString { "/{${it.name}}" },
                arguments = arguments,
        ) {
            Screen(navController = navController, entry = it)
        }
    }
}

