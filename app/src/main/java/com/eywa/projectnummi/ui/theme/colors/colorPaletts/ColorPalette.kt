package com.eywa.projectnummi.ui.theme.colors.colorPaletts

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.ui.theme.colors.BaseColor
import com.eywa.projectnummi.ui.theme.colors.ThemeColor

@Immutable
data class ColorPalette(
        val unassignedColor: Color = Color.Transparent,

        val statusBar: Color = BaseColor.BASE_PURPLE,
        val androidNavButtons: Color = BaseColor.BASE_PURPLE,

        val appBackground: ThemeColor = ThemeColor(main = BaseColor.BASE_BLACK, content = Color.White),
        val navBar: ThemeColor = ThemeColor(main = BaseColor.BASE_SPACE, content = Color.White),

        val outlinedTextField: @Composable () -> TextFieldColors = {
            TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    disabledTextColor = BaseColor.GREY,

                    backgroundColor = BaseColor.BASE_SPACE,

                    cursorColor = BaseColor.BASE_PURPLE,
                    errorCursorColor = BaseColor.BASE_ERROR,

                    focusedBorderColor = BaseColor.BASE_BLUE,
                    unfocusedBorderColor = BaseColor.BASE_PURPLE,
                    disabledBorderColor = BaseColor.GREY,
                    errorBorderColor = BaseColor.BASE_ERROR,

                    leadingIconColor = BaseColor.BASE_PURPLE,
                    disabledLeadingIconColor = BaseColor.GREY,
                    errorLeadingIconColor = BaseColor.BASE_ERROR,

                    trailingIconColor = BaseColor.BASE_PURPLE,
                    disabledTrailingIconColor = BaseColor.GREY,
                    errorTrailingIconColor = BaseColor.BASE_ERROR,

                    focusedLabelColor = BaseColor.BASE_BLUE,
                    unfocusedLabelColor = BaseColor.BASE_PURPLE,
                    disabledLabelColor = BaseColor.GREY,
                    errorLabelColor = BaseColor.BASE_ERROR,

                    placeholderColor = BaseColor.BASE_PURPLE.copy(alpha = 0.5f),
                    disabledPlaceholderColor = BaseColor.GREY.copy(alpha = 0.5f),
            )
        }
)
