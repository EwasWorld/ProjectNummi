package com.eywa.projectnummi.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import com.eywa.projectnummi.features.addTransactions.AddTransactionsScreen
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsScreen
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesScreen
import com.eywa.projectnummi.features.managePeople.ManagePeopleScreen
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsScreen

fun String.toMainNavRoute() =
        try {
            MainNavRoute.valueOf(uppercase())
        }
        catch (e: IllegalArgumentException) {
            null
        }

enum class MainNavRoute : NavRoute {
    ADD_TRANSACTIONS {
        @Composable
        override fun Screen(navController: NavHostController) = AddTransactionsScreen()

        override fun saveStateOnNavigate(arguments: Map<String, NavArgument>?): Boolean {
            return true
        }
    },
    VIEW_TRANSACTIONS {
        @Composable
        override fun Screen(navController: NavHostController) = ViewTransactionsScreen()
    },
    MANAGE_CATEGORIES {
        @Composable
        override fun Screen(navController: NavHostController) = ManageCategoriesScreen()
    },
    MANAGE_PEOPLE {
        @Composable
        override fun Screen(navController: NavHostController) = ManagePeopleScreen()
    },
    MANAGE_ACCOUNTS {
        @Composable
        override fun Screen(navController: NavHostController) = ManageAccountsScreen()
    },
    ;

    override val routeBase = "mainnavroute_" + name.lowercase()

    open fun saveStateOnNavigate(arguments: Map<String, NavArgument>?): Boolean = false
}
