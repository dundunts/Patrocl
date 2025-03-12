package org.turter.patrocl.presentation.components.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.turter.patrocl.utils.toFormattedString

@Composable
fun RkQuantityInputField(
    modifier: Modifier = Modifier,
    initQnt: Int,
    onQuantityChange: (Int) -> Unit,
    qntDigits: Int,
    isError: Boolean = false,
    errorText: String? = null
) {
    var text by remember { mutableStateOf(formatQuantity(initQnt, qntDigits)) }
    val keyboardOptions = if (qntDigits == 0) {
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    } else {
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
    }

    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            val formattedInput = filterInput(input, qntDigits)
            text = formattedInput
            onQuantityChange(parseQuantity(formattedInput, qntDigits))
        },
        label = { Text("Кол-во") },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        isError = isError,
        supportingText = errorText?.let { { Text(it) } }
    )
}

private fun formatQuantity(quantity: Int, qntDigits: Int): String {
    return if (qntDigits == 0) {
        (quantity / 1000).toString()
    } else {
        (quantity / 1000.0f).toFormattedString(qntDigits)
    }
}

private fun filterInput(input: String, qntDigits: Int): String {
    val regex = if (qntDigits == 0) {
        "\\d*"
    } else {
        "\\d*(\\.\\d{0,$qntDigits})?"
    }
    return if (input.matches(regex.toRegex())) input else input.dropLast(1)
}

private fun parseQuantity(input: String, qntDigits: Int): Int {
    return if (input.isBlank()) 0 else (input.toFloat() * 1000).toInt()
}