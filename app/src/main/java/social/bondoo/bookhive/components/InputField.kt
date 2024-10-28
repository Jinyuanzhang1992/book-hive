package social.bondoo.bookhive.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    config: InputFieldProps = InputFieldProps()
) {
    val visualTransformation = if (config.isPasswordField && !config.passwordVisibility.value) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }
    OutlinedTextField(
        value = config.valueState.value,
        onValueChange = { config.valueState.value = it.trim() },
        label = { Text(text = config.label) },
//        maxLines = config.maxLines,
        singleLine = config.isSingleLine,
        enabled = config.enabled,
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.secondary,
        ),
        keyboardActions = config.onAction,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = config.keyboardType,
            imeAction = config.imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (config.isPasswordField) {
                PasswordVisibility(config.passwordVisibility)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

data class InputFieldProps(
    var valueState: MutableState<String> = mutableStateOf(""),
    var label: String = "",
    var enabled: Boolean = true,
    var isSingleLine: Boolean = true,
    var maxLines: Int = 10,
    var keyboardType: KeyboardType = KeyboardType.Text,
    var imeAction: ImeAction = ImeAction.Next,
    var onAction: KeyboardActions = KeyboardActions.Default,
    var isPasswordField: Boolean = false,
    var passwordVisibility: MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        Icon(
            imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (visible) "Hide password" else "Show password"
        )
    }
}
