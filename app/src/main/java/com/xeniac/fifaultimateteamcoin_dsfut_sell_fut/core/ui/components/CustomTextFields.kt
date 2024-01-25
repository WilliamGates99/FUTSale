package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun CustomOutlinedTextField(
    isLoading: Boolean,
    value: String,
    title: String?,
    placeholder: String?,
    errorText: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = errorText != null,
    isPasswordTextField: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleMaxLines: Int = 1,
    titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textFontSize: TextUnit = 16.sp,
    textLineHeight: TextUnit = TextUnit.Unspecified,
    textFontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Unspecified,
    textDirection: TextDirection = TextDirection.Unspecified,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = textFontSize,
        lineHeight = textLineHeight,
        fontWeight = textFontWeight,
        textAlign = textAlign,
        textDirection = textDirection
    ),
    supportingText: String? = null,
    supportingTextFontSize: TextUnit = 12.sp,
    supportingTextFontWeight: FontWeight = FontWeight.Normal,
    supportingTextColor: Color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
    placeholderFontSize: TextUnit = 16.sp,
    placeholderFontWeight: FontWeight = FontWeight.Normal,
    placeholderMaxLines: Int = 1,
    placeholderTextAlign: TextAlign? = null,
    leadingIcon: Painter? = null,
    leadingIconContentDescription: String? = null,
    leadingIconSize: Dp = 24.dp,
    trailingIcon: Painter? = null,
    trailingIconContentDescription: String? = null,
    trailingIconSize: Dp = 24.dp,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    autoCorrect: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = imeAction,
        capitalization = keyboardCapitalization,
        autoCorrect = autoCorrect
    ),
    spaceBetweenTitleAndTextField: Dp = 4.dp,
    onValueChange: (newValue: String) -> Unit,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    keyboardAction: () -> Unit = {}
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        title?.let { title ->
            Text(
                text = title,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                maxLines = titleMaxLines,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(spaceBetweenTitleAndTextField))
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled && !isLoading,
            readOnly = readOnly,
            isError = isError,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            shape = shape,
            colors = colors,
            textStyle = textStyle,
            label = null,
            prefix = prefix,
            suffix = suffix,
            placeholder = if (placeholder != null) {
                {
                    Text(
                        text = placeholder,
                        fontSize = placeholderFontSize,
                        fontWeight = placeholderFontWeight,
                        maxLines = placeholderMaxLines,
                        textAlign = placeholderTextAlign
                    )
                }
            } else null,
            supportingText = when {
                supportingText != null -> {
                    {
                        Text(
                            text = supportingText,
                            fontSize = supportingTextFontSize,
                            fontWeight = supportingTextFontWeight,
                            color = supportingTextColor
                        )
                    }
                }
                errorText != null -> {
                    {
                        Text(
                            text = errorText,
                            fontSize = supportingTextFontSize,
                            fontWeight = supportingTextFontWeight,
                            color = supportingTextColor
                        )
                    }
                }
                else -> null
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = leadingIconContentDescription,
                        modifier = Modifier.size(leadingIconSize)
                    )
                }
            } else null,
            trailingIcon = when {
                isPasswordTextField -> {
                    {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                            Icon(
                                painter = if (isPasswordVisible) painterResource(
                                    id = R.drawable.ic_password_toggle_visible
                                ) else painterResource(id = R.drawable.ic_password_toggle_invisible),
                                contentDescription = if (isPasswordVisible) stringResource(
                                    id = R.string.core_textfield_content_description_password_toggle_hide
                                ) else stringResource(id = R.string.core_textfield_content_description_password_toggle_show)
                            )
                        }
                    }
                }
                trailingIcon != null -> {
                    {
                        Icon(
                            painter = trailingIcon,
                            contentDescription = trailingIconContentDescription,
                            modifier = Modifier.size(trailingIconSize)
                        )
                    }
                }
                else -> null
            },
            visualTransformation = if (isPasswordTextField && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions {
                defaultKeyboardAction(imeAction)
                keyboardAction()
            }
        )
    }
}