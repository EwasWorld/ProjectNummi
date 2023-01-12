package com.eywa.projectnummi.sharedUi.utils

import com.eywa.projectnummi.model.NamedItem
import com.eywa.projectnummi.navigation.NummiNavRoute

enum class ManageTabSwitcherItem(val text: String, val navRoute: NummiNavRoute) : NamedItem {
    CATEGORIES("Categories", NummiNavRoute.MANAGE_CATEGORIES),
    PEOPLE("People", NummiNavRoute.MANAGE_PEOPLE),
    ACCOUNTS("Accounts", NummiNavRoute.MANAGE_ACCOUNTS),
    ;

    override fun getItemName(): String = text
}
