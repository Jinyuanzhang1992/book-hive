package social.bondoo.bookhive.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import social.bondoo.bookhive.model.MBook
import social.bondoo.bookhive.navigation.BookHiveScreens

@Composable
fun BooksList(
    books: List<MBook>,
    title: String = "Reading Books",
    cardType: String = "reading",
    buttonLabel: String = "Reading",
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = when (cardType) {
            "reading" -> Alignment.Start
            "not-started" -> Alignment.End
            else -> Alignment.CenterHorizontally
        }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
        Card {
            LazyRow {
                items(books) { book ->
                    ShowBooksCard(book = book, buttonLabel = buttonLabel) {
                        navController.navigate(BookHiveScreens.UpdateBookScreen.name + "/$it")
                    }
                }
            }
        }
    }
}

