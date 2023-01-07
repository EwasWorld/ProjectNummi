package com.eywa.projectnummi.components.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import com.eywa.projectnummi.model.Account
import com.eywa.projectnummi.ui.theme.NummiTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountItem(
        account: Account?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
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
