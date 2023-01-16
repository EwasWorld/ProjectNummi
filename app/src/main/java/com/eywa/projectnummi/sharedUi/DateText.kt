package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.theme.asClickableStyle
import com.eywa.projectnummi.utils.DateTimeFormat
import java.util.*

@Composable
fun DateText(
        style: TextStyle,
        date: Calendar,
        onChange: (Calendar) -> Unit,
) {
    val context = LocalContext.current
    val dialogTheme = NummiTheme.colors.dialogThemeId
    val datePicker by lazy {
        NummiDatePicker.createDialog(context, date, dialogTheme) { onChange(it) }
    }

    Text(
            text = DateTimeFormat.LONG_DATE.format(date),
            style = style.asClickableStyle(),
            modifier = Modifier.clickable { datePicker.show() }
    )
}
