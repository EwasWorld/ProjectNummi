package com.eywa.projectnummi.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import com.eywa.projectnummi.features.addTransactions.stripNewLines
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun NummiTextField(
        text: String,
        onTextChanged: (String) -> Unit,
        label: String,
        placeholderText: String,
        imeAction: ImeAction = ImeAction.Next
) = OutlinedTextField(
        value = text,
        onValueChange = { onTextChanged(it.stripNewLines()) },
        keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
        ),
        label = {
            Text(
                    text = label
            )
        },
        placeholder = {
            Text(
                    text = placeholderText
            )
        },
        colors = NummiTheme.colors.outlinedTextField()
)
