package social.bondoo.bookhive.screens.updateBook

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import social.bondoo.bookhive.screens.home.HomeScreenViewModel
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType

@Composable
fun UpdateBookScreen(
    navController: NavHostController,
    bookItemId: String,
    homeScreenViewModel: HomeScreenViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Layout(
        navController = navController,
        title = "Update Book",
        showBottomBar = true,
        topBarType = TopBarType.UpdateScreen,
        showIcon = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UpdateBookScreenContent(
                navController = navController,
                bookItemId = bookItemId,
                homeScreenViewModel = homeScreenViewModel,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun UpdateBookScreenContent(
    navController: NavHostController,
    bookItemId: String,
    homeScreenViewModel: HomeScreenViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val bookListFromDB = homeScreenViewModel.data.value.data
    val selectedBook = bookListFromDB?.find { it.id == bookItemId }
    val imageUrl = selectedBook?.photoUrl ?: "https://picsum.photos/200/300"
    val comment = remember(selectedBook?.notes) {
        mutableStateOf(selectedBook?.notes ?: "Great Book")
    }
    val rating = remember(selectedBook?.rating) {
        mutableIntStateOf(selectedBook?.rating?.toInt() ?: 0)
    }
    val isStartedReading = remember { mutableStateOf(false) }
    val isFinishedReading = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SummarySection(imageUrl, selectedBook)
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommentSection(comment)
            Spacer(modifier = Modifier.height(30.dp))
            StatusChangeSection(
                selectedBook,
                isStartedReading = isStartedReading,
                isFinishedReading = isFinishedReading
            )
            Spacer(modifier = Modifier.height(30.dp))
            RatingSection(rating)
            Spacer(modifier = Modifier.height(30.dp))
            UpdateSection { it ->
                if (it == "Update") {
                    val changedComment = selectedBook?.notes != comment.value
                    val changedRating = selectedBook?.rating?.toInt() != rating.intValue
                    val updatedStartedTimeStamp =
                        if (isStartedReading.value) Timestamp.now() else selectedBook?.startedReading
                    val updatedFinishedTimeStamp =
                        if (isFinishedReading.value) Timestamp.now() else selectedBook?.finishedReading
                    val bookUpdate =
                        changedComment || changedRating || isFinishedReading.value || isStartedReading.value
                    val bookToUpdate = hashMapOf(
                        "notes" to comment.value,
                        "rating" to rating.intValue,
                        "finished_reading_at" to updatedFinishedTimeStamp,
                        "started_reading_at" to updatedStartedTimeStamp
                    ).toMap()
                    if (bookUpdate) {
                        FirebaseFirestore.getInstance()
                            .collection("books")
                            .document(selectedBook?.id!!)
                            .update(bookToUpdate)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Update Successful")
                                        delay(200)
                                        navController.popBackStack()
                                    }
                                } else {
                                    Log.d(
                                        "UpdateBookScreenContent",
                                        "Error updating document",
                                        task.exception
                                    )
                                }
                            }
                            .addOnFailureListener {
                                Log.w("UpdateBookScreenContent", "Error updating document", it)
                            }
                    }
                } else {
                    openDialog.value = true
                }
            }
        }
    }
    if (openDialog.value) {
        ShowAlertDialog(
            title = "Are you sure to delete this book?",
            onConfirm = {
                openDialog.value = false
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(selectedBook?.id!!)
                    .delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Delete Successful")
                                delay(300)
                                navController.popBackStack()
                            }
                        } else {
                            Log.d(
                                "UpdateBookScreenContent",
                                "Error deleting document",
                                task.exception
                            )
                        }
                    }
            },
            onDismiss = { openDialog.value = false }
        )
    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = "This action cannot be undone",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("Yes", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("No", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

@Composable
fun UpdateSection(onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        UpdateButton(
            label = "Delete",
            onClick = { onClick("Delete") }
        )
        UpdateButton(
            label = "Update",
            onClick = { onClick("Update") }
        )
    }
}

@Composable
fun UpdateButton(
    label: String = "Mark as Read",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}






