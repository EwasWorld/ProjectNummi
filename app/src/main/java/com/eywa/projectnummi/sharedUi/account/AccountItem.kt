package com.eywa.projectnummi.sharedUi.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.eywa.projectnummi.model.Account
import com.eywa.projectnummi.sharedUi.BorderedItem
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun AccountItem(
        account: Account?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    BorderedItem(
            onClick = onClick,
            modifier = modifier
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                    text = account?.name ?: "Default account",
                    color = NummiTheme.colors.appBackground.content,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (account?.type != null) {
                Text(
                        text = account.type,
                        color = NummiTheme.colors.appBackground.content,
                        fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Composable
fun AccountItem(
        accounts: List<Account?>,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    BorderedItem(
            onClick = onClick,
            modifier = modifier
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                    text = when (accounts.size) {
                        0 -> "All accounts"
                        1 -> accounts.first()?.name ?: "Default account"
                        else -> "Various accounts"
                    },
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}
