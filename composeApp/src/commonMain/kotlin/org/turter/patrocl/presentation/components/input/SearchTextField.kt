package org.turter.patrocl.presentation.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onTextLayout: (TextLayoutResult) -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    shape: Shape = RoundedCornerShape(8.dp),
) {
    val focusManager = LocalFocusManager.current

    SimpleTextField(
        modifier = modifier,
        shape = shape,
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        trailingIcon = onClearClick?.let {
            {
                Icon(
                    modifier = Modifier.clickable(onClick = onClearClick),
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear icon"
                )
            }
        },
        placeholder = placeholder,
        textStyle = TextStyle(
            color = textColor
        ),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(textColor),
        colors = colors,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        ),
        onTextLayout = onTextLayout
    )
}