package social.bondoo.bookhive.screens.addBook

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import social.bondoo.bookhive.components.CustomTextField
import social.bondoo.bookhive.components.InputFieldProps
import social.bondoo.bookhive.model.BookHive
import social.bondoo.bookhive.model.Item
import social.bondoo.bookhive.navigation.BookHiveScreens
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType

@Composable
fun AddBookScreen(navController: NavHostController, addBookViewModel: AddBookViewModel) {
    Layout(
        navController = navController,
        title = "Add Book",
        showBottomBar = true,
        topBarType = TopBarType.AddBookScreen
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AddBookScreenContent(navController = navController, addBookViewModel = addBookViewModel)
        }
    }
}

@Composable
fun AddBookScreenContent(navController: NavHostController, addBookViewModel: AddBookViewModel) {
    val bookState = rememberSaveable { mutableStateOf("") }
    val valid = rememberSaveable(bookState.value) {
        mutableStateOf(
            bookState.value.trim().isNotEmpty()
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    CustomTextField(
        config = InputFieldProps(
            valueState = bookState,
            label = "Search Books",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                if (!valid.value) return@KeyboardActions
                keyboardController?.hide()
                addBookViewModel.getBooks(bookState.value)
                bookState.value = ""
            },
        )
    )
    Spacer(modifier = Modifier.height(20.dp))
    val listOfBooks = addBookViewModel.list
    if (addBookViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,

            ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(listOfBooks) { book ->
                BookItem(book) { id ->
                    Log.d("id", "AddBookScreenContent: $id")
                    navController.navigate(BookHiveScreens.DetailScreen.name + "/${book.id}")
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

    }


}

@Composable
fun BookItem(
    book: Item,
    onClick: (String) -> Unit = {}
) {
    val imageUrl = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty {
        "https://picsum.photos/200/300"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        onClick = { onClick(book.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RectangleShape)
                    .fillMaxWidth(0.3f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Authors: ${book.volumeInfo.authors?.joinToString(", ") ?: "No Authors"}",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Published on ${book.volumeInfo.publishedDate}",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Category: ${book.volumeInfo.categories?.joinToString(", ") ?: "No Categories"}",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                )
            }


        }
    }
}