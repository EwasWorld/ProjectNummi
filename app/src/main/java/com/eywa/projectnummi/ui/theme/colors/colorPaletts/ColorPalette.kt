package com.eywa.projectnummi.ui.theme.colors.colorPaletts

import androidx.compose.material.*
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
        val fab: ThemeColor = ThemeColor(main = BaseColor.BASE_BLUE, content = BaseColor.BASE_SPACE),
        val dialog: ThemeColor = ThemeColor(main = BaseColor.BASE_BLACK, content = Color.White),

        val link: Color = BaseColor.BLUE,

        val generalButton: @Composable () -> ButtonColors = {
            ButtonDefaults.buttonColors(
                    backgroundColor = BaseColor.BASE_BLUE,
                    contentColor = BaseColor.BASE_SPACE,
                    disabledBackgroundColor = BaseColor.BASE_SPACE,
                    disabledContentColor = BaseColor.GREY_500,
            )
        },
        val primaryTextButton: @Composable () -> ButtonColors = {
            ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    disabledContentColor = BaseColor.GREY_500,
            )
        },
        val secondaryTextButton: @Composable () -> ButtonColors = {
            ButtonDefaults.textButtonColors(
                    contentColor = BaseColor.BASE_PURPLE,
                    disabledContentColor = BaseColor.GREY_500,
            )
        },
        val generalCheckbox: @Composable () -> CheckboxColors = {
            CheckboxDefaults.colors(
                    checkedColor = BaseColor.BASE_PURPLE,
                    uncheckedColor = BaseColor.BASE_PURPLE,
                    checkmarkColor = BaseColor.BASE_SPACE,
                    disabledColor = BaseColor.GREY_500,
                    disabledIndeterminateColor = BaseColor.GREY_500,
            )
        },
        val generalSlider: @Composable () -> SliderColors = {
            SliderDefaults.colors(
                    thumbColor = BaseColor.BASE_PURPLE,
                    disabledThumbColor = BaseColor.GREY_500,

                    activeTrackColor = BaseColor.BASE_PURPLE,
                    inactiveTrackColor = BaseColor.BASE_SPACE,
                    disabledActiveTrackColor = BaseColor.GREY_500,
                    disabledInactiveTrackColor = BaseColor.GREY_300,

                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    disabledActiveTickColor = Color.Transparent,
                    disabledInactiveTickColor = Color.Transparent,
            )
        },
        val outlinedTextField: @Composable () -> TextFieldColors = {
            TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    disabledTextColor = BaseColor.GREY_500,

                    backgroundColor = BaseColor.BASE_SPACE,

                    cursorColor = BaseColor.BASE_PURPLE,
                    errorCursorColor = BaseColor.RED,

                    focusedBorderColor = BaseColor.BASE_BLUE,
                    unfocusedBorderColor = BaseColor.BASE_PURPLE,
                    disabledBorderColor = BaseColor.GREY_500,
                    errorBorderColor = BaseColor.RED,

                    leadingIconColor = BaseColor.BASE_PURPLE,
                    disabledLeadingIconColor = BaseColor.GREY_500,
                    errorLeadingIconColor = BaseColor.RED,

                    trailingIconColor = BaseColor.BASE_PURPLE,
                    disabledTrailingIconColor = BaseColor.GREY_500,
                    errorTrailingIconColor = BaseColor.RED,

                    focusedLabelColor = BaseColor.BASE_BLUE,
                    unfocusedLabelColor = BaseColor.BASE_PURPLE,
                    disabledLabelColor = BaseColor.GREY_500,
                    errorLabelColor = BaseColor.RED,

                    placeholderColor = BaseColor.BASE_PURPLE.copy(alpha = 0.5f),
                    disabledPlaceholderColor = BaseColor.GREY_500.copy(alpha = 0.5f),
            )
        },
)
