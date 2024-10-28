package social.bondoo.bookhive.screens.updateBook

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import social.bondoo.bookhive.components.CustomTextField
import social.bondoo.bookhive.components.InputFieldProps

@Composable
fun CommentSection(
    comment: MutableState<String>,
) {
    val valid = remember(comment.value) {
        comment.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    CustomTextField(
        modifier = Modifier.height(180.dp),
        config = InputFieldProps(
            valueState = comment,
            label = "Enter Your Comments",
            maxLines = 5,
            isSingleLine = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                keyboardController?.hide()
            },
        )
    )
}
