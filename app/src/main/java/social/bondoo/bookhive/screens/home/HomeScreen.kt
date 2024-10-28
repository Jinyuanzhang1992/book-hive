package social.bondoo.bookhive.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import social.bondoo.bookhive.model.MBook

@Composable
fun HomeScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel) {
    Layout(
        navController = navController,
        title = "Book Hive",
        showBottomBar = true,
        topBarType = TopBarType.HomeScreen,
        showIcon = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HomeScreenContent(
                navController = navController,
                homeScreenViewModel = homeScreenViewModel
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
) {
    homeScreenViewModel.getAllBooksFromDB()
    var notStartedBooks = remember {
        listOf<MBook>()
    }
    var readingBooks = remember {
        listOf<MBook>()
    }
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (!homeScreenViewModel.data.value.data.isNullOrEmpty()) {
        notStartedBooks = homeScreenViewModel.data.value.data!!.toList().filter {
            it.userId == currentUser?.uid.toString() && it.startedReading == null
        }
        readingBooks = homeScreenViewModel.data.value.data!!.toList().filter {
            it.userId == currentUser?.uid.toString() && it.startedReading != null && it.finishedReading == null
        }
    }
    if (readingBooks.isNotEmpty()) {
        BooksList(
            books = readingBooks,
            title = "Reading Books",
            cardType = "reading",
            buttonLabel = "Reading",
            navController = navController
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
    if (notStartedBooks.isNotEmpty()) {
        BooksList(
            books = notStartedBooks,
            title = "Not Started Books",
            cardType = "not-started",
            buttonLabel = "Not Started",
            navController = navController
        )
    }
}

