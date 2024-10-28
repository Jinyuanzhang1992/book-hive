package social.bondoo.bookhive.screens.updateBook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import social.bondoo.bookhive.model.MBook
import social.bondoo.bookhive.utils.formatDate

@Composable
fun StatusChangeSection(
    book: MBook?,
    isStartedReading: MutableState<Boolean>,
    isFinishedReading: MutableState<Boolean>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StartButton(isStartedReading, book)
            FinishButton(isFinishedReading, book)
        }
    }
}

@Composable
private fun StartButton(
    isStartedReading: MutableState<Boolean>,
    book: MBook?
) {
    TextButton(
        onClick = { isStartedReading.value = !isStartedReading.value },
        enabled = book?.startedReading == null,
        modifier = Modifier.width(170.dp)
    ) {
        if (book?.startedReading == null) {
            if (!isStartedReading.value) {
                Text(
                    text = "Start Reading!",
                    style = MaterialTheme.typography.titleMedium,
                    color =MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Started Reading",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text(
                text = "Started On: ${formatDate(book.startedReading!!)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
private fun FinishButton(
    isFinishedReading: MutableState<Boolean>,
    book: MBook?
) {
    TextButton(
        onClick = { isFinishedReading.value = !isFinishedReading.value },
        enabled = book?.finishedReading == null,
        modifier = Modifier.width(170.dp)
    ) {
        if (book?.finishedReading == null) {
            if (!isFinishedReading.value) {
                Text(
                    text = "Mark as Read",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Finished Reading!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text(
                text = "Finished On: ${formatDate(book.finishedReading!!)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}