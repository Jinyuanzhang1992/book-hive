package social.bondoo.bookhive.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType

@Composable
fun DetailsScreen(
    navController: NavHostController,
    bookId: String,
    detailsScreenViewModel: DetailsScreenViewModel,
) {
    Layout(
        navController = navController,
        title = "Book Details",
        showBottomBar = false,
        topBarType = TopBarType.DetailsScreen,
        showIcon = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DetailsScreenContent(
                navController = navController,
                bookId = bookId,
                detailsScreenViewModel = detailsScreenViewModel
            )
        }
    }
}

@Composable
fun DetailsScreenContent(
    bookId: String,
    navController: NavHostController,
    detailsScreenViewModel: DetailsScreenViewModel
) {
    LaunchedEffect(bookId) {
        detailsScreenViewModel.getBookById(bookId)
    }
    val bookDetails = detailsScreenViewModel.book
    if (detailsScreenViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } else {
        bookDetails?.let {
            BookDetails(navController = navController, book = it)
        }
    }
}



