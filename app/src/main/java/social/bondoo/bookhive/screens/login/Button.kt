package social.bondoo.bookhive.screens.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Button(
    valid: Boolean,
    title: String = "Login",
    onClick: () -> Unit = {}
) {
    androidx.compose.material3.Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            onClick()
        },
        enabled = valid
    ) {
        Text(
            text = title,
            color = when (valid) {
                true -> MaterialTheme.colorScheme.onPrimary
                false -> MaterialTheme.colorScheme.primary
            },
            textAlign = TextAlign.Center
        )
    }
}