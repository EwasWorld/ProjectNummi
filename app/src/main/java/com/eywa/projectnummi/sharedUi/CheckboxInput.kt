package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun CheckboxInput(
        text: String,
        isSelected: Boolean,
        onClick: () -> Unit,
) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
                text = text,
                color = NummiTheme.colors.appBackground.content,
        )
        Checkbox(
                checked = isSelected,
                onCheckedChange = { onClick() },
                colors = NummiTheme.colors.generalCheckbox(),
        )
    }
}
