package com.eywa.projectnummi.features.transactionsSummary.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.database.transaction.TransactionsFilters
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent.*
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryGrouping
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.sharedUi.CheckboxInput
import com.eywa.projectnummi.sharedUi.DateText
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.theme.asClickableStyle

@Composable
fun TransactionsSummaryFiltersScreen(
        state: TransactionsSummaryState,
        listener: (TransactionsSummaryIntent) -> Unit,
) {
    Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                    text = "Group by",
                    color = NummiTheme.colors.appBackground.content,
            )
            Text(
                    text = state.currentGrouping.toDisplayString(),
                    style = NummiTheme.typography.h5.asClickableStyle(),
                    modifier = Modifier.clickable { listener(GroupingClicked) }
            )
        }
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
        ) {
            DateText(
                    style = NummiTheme.typography.h5,
                    date = state.filtersState.from,
                    onChange = { listener(FromDateChanged(it)) },
            )
            Text(
                    text = "to",
                    color = NummiTheme.colors.appBackground.content,
            )
            DateText(
                    style = NummiTheme.typography.h5,
                    date = state.filtersState.to,
                    onChange = { listener(ToDateChanged(it)) },
            )
        }
        AccountItem(
                accounts = state.selectedAccounts,
                onClick = { listener(AccountClicked) },
        )
        CategoryItem(
                categories = state.selectedCategories,
                onClick = { listener(CategoryClicked) },
        )
        PersonItem(
                people = state.selectedPeople,
                onClick = { listener(PersonClicked) },
        )
        CheckboxInput(
                text = "Show incoming",
                isSelected = state.filtersState.showIncoming,
                onClick = { listener(ToggleShowIncoming) },
        )
        CheckboxInput(
                text = "Show outgoing",
                isSelected = state.filtersState.showOutgoing,
                onClick = { listener(ToggleShowOutgoing) },
        )
        CheckboxInput(
                text = "Outgoing is positive",
                isSelected = state.outgoingIsPositive,
                onClick = { listener(ToggleOutgoingIsPositive) },
        )
    }
}

@Composable
fun TransactionsSummaryGrouping.toDisplayString() = when (this) {
    TransactionsSummaryGrouping.CATEGORY -> "Category"
    TransactionsSummaryGrouping.PERSON -> "Person"
    TransactionsSummaryGrouping.ACCOUNT -> "Account"
    TransactionsSummaryGrouping.OUTGOING -> "Outgoing"
}

@Preview
@Composable
fun TransactionsSummaryFiltersScreen_Preview(
        @PreviewParameter(TransactionsSummaryFiltersPreviewParamProvider::class)
        params: TransactionsSummaryFiltersPreviewParams,
) {
    NummiScreenPreviewWrapper {
        TransactionsSummaryFiltersScreen(
                TransactionsSummaryState(
                        accounts = AccountProvider.basic,
                        categories = CategoryProvider.basic,
                        people = PeopleProvider.basic,
                        filtersState = TransactionsFilters(
                                selectedAccountIds = params.selectedAccountIds,
                                selectedCategoryIds = params.selectedCategoryIds,
                                selectedPersonIds = params.selectedPersonIds,
                        ),
                ),
        ) {}
    }
}

data class TransactionsSummaryFiltersPreviewParams(
        val selectedAccountIds: List<Int?> = emptyList(),
        val selectedCategoryIds: List<Int> = emptyList(),
        val selectedPersonIds: List<Int?> = emptyList(),
)

class TransactionsSummaryFiltersPreviewParamProvider
    : CollectionPreviewParameterProvider<TransactionsSummaryFiltersPreviewParams>(
        listOf(
                TransactionsSummaryFiltersPreviewParams(),
                TransactionsSummaryFiltersPreviewParams(
                        selectedAccountIds = AccountProvider.basic.take(1).map { it.id },
                        selectedCategoryIds = CategoryProvider.basic.take(1).map { it.id },
                        selectedPersonIds = PeopleProvider.basic.take(1).map { it.id },
                ),
                TransactionsSummaryFiltersPreviewParams(
                        selectedAccountIds = AccountProvider.basic.take(2).map { it.id },
                        selectedCategoryIds = CategoryProvider.basic.take(2).map { it.id },
                        selectedPersonIds = PeopleProvider.basic.take(2).map { it.id },
                ),
        )
)
