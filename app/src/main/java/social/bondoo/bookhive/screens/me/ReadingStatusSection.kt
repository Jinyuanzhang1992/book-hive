package social.bondoo.bookhive.screens.me

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import social.bondoo.bookhive.model.MBook

@Composable
fun ReadingStatusSection(
    userBooks: List<MBook>,
    readingBooksCount: Int,
    readBooksCount: Int,
    notStartedBooksCount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Reading Status",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatusSummary(
                readingCount = readingBooksCount,
                readCount = readBooksCount,
                notStartedBooksCount = notStartedBooksCount
            )
            Spacer(modifier = Modifier.height(20.dp))
            StatusList(userBooks = userBooks)
        }
    }
}

@Composable
fun StatusList(userBooks: List<MBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        items(userBooks) { book ->
            BookStatusCard(book = book)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


