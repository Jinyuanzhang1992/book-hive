package social.bondoo.bookhive.screens.details

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import social.bondoo.bookhive.model.Item
import social.bondoo.bookhive.model.MBook
import social.bondoo.bookhive.navigation.BookHiveScreens

@Composable
fun BookDetails(
    navController: NavHostController,
    book: Item
) {
    val imageUrl = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty {
        "https://picsum.photos/200/300"
    }
    val cleanDescription =
        HtmlCompat.fromHtml(book.volumeInfo.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            .toString()
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
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
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(40.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BookDetailsButton(label = "Back") { navController.popBackStack() }
        BookDetailsButton(label = "Save") {
            val bookToFireBase = MBook(
                title = book.volumeInfo.title,
                authors = book.volumeInfo.authors.joinToString(", "),
                description = book.volumeInfo.description,
                pageCount = book.volumeInfo.pageCount.toString(),
                categories = book.volumeInfo.categories.joinToString(", "),
                publishedDate = book.volumeInfo.publishedDate,
                photoUrl = book.volumeInfo.imageLinks.smallThumbnail,
                notes = "",
                rating = 0.0,
                googleBookId = book.id,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            )
            saveToFirebase(bookToFireBase,navController = navController)
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Authors:",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = book.volumeInfo.authors?.joinToString(", ") ?: "No Authors",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Page Count:",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${book.volumeInfo.pageCount}",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Categories:",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = book.volumeInfo.categories?.joinToString(", ") ?: "No Categories",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Published Date:",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = book.volumeInfo.publishedDate,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column {
            Text(
                text = "Description: ",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = cleanDescription,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun saveToFirebase(book: MBook, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                dbCollection.document(documentId)
                    .update(hashMapOf("id" to documentId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("save", "saveToFirebase: DocumentSnapshot added with ID: $documentId")
                            navController.navigate(BookHiveScreens.HomeScreen.name)
                        } else {
                            Log.d("save", "saveToFirebase: Error adding document", task.exception)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("save", "saveToFirebase: Error adding document", it)
                    }
            }
    } else {
        Log.d("save", "saveToFirebase: No data to save")
    }
}

@Composable
fun BookDetailsButton(
    label: String = "Save",
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.width(100.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}