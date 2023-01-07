package com.eywa.projectnummi.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.eywa.projectnummi.features.addTransactions.AddTransactionsScreen
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsScreen
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesScreen
import com.eywa.projectnummi.features.managePeople.ManagePeopleScreen
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsScreen

enum class MainNavRoute : NavRoute {
    ADD_TRANSACTIONS {
        override val routeBase: String = "add_transactions"

        @Composable
        override fun Screen(navController: NavHostController) = AddTransactionsScreen()
    },
    VIEW_TRANSACTIONS {
        override val routeBase: String = "view_transactions"

        @Composable
        override fun Screen(navController: NavHostController) = ViewTransactionsScreen()
    },
    MANAGE_CATEGORIES {
        override val routeBase: String = "manage_categories"

        @Composable
        override fun Screen(navController: NavHostController) = ManageCategoriesScreen()
    },
    MANAGE_PEOPLE {
        override val routeBase: String = "manage_people"

        @Composable
        override fun Screen(navController: NavHostController) = ManagePeopleScreen()
    },
    MANAGE_ACCOUNTS {
        override val routeBase: String = "manage_accounts"

        @Composable
        override fun Screen(navController: NavHostController) = ManageAccountsScreen()
    },
}
