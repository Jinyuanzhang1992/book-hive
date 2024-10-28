package social.bondoo.bookhive.screens.me

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import social.bondoo.bookhive.model.MBook
import social.bondoo.bookhive.screens.home.HomeScreenViewModel
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType

@Composable
fun MeScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel) {
    Layout(
        navController = navController,
        showBottomBar = true,
        topBarType = TopBarType.MeScreen,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MeScreenContent(
                navController = navController,
                homeScreenViewModel = homeScreenViewModel
            )
        }
    }
}

@Composable
fun MeScreenContent(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserName = if (currentUser?.email?.isNotEmpty() == true) {
        currentUser.email?.split("@")?.get(0)
    } else {
        "N/A"
    }
    val userEmail = if (currentUser?.email?.isNotEmpty() == true) {
        currentUser.email
    } else {
        "N/A"
    }
    var userBooks = remember { listOf<MBook>() }
    if (!homeScreenViewModel.data.value.data.isNullOrEmpty()) {
        userBooks = homeScreenViewModel.data.value.data!!.toList().filter {
            it.userId == currentUser?.uid.toString()
        }
    }
    val readingBooksCount = remember { mutableIntStateOf(0) }
    val readBooksCount = remember { mutableIntStateOf(0) }
    val notStartedBooksCount = remember { mutableIntStateOf(0) }
    if (userBooks.isNotEmpty()) {
        notStartedBooksCount.intValue = userBooks.filter { it.startedReading == null }.size
        readBooksCount.intValue =
            userBooks.filter { it.finishedReading != null && it.startedReading != null }.size
        readingBooksCount.intValue =
            userBooks.filter { it.startedReading != null && it.finishedReading == null }.size
    }
    AvatarSection(
        navController = navController,
        currentUserName = currentUserName,
        userEmail = userEmail
    )
    Spacer(modifier = Modifier.height(50.dp))
    ReadingStatusSection(
        userBooks = userBooks,
        readingBooksCount = readingBooksCount.intValue,
        readBooksCount = readBooksCount.intValue,
        notStartedBooksCount = notStartedBooksCount.intValue
    )
}

