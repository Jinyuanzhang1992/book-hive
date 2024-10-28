package social.bondoo.bookhive.screens.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Person4
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import social.bondoo.bookhive.navigation.BookHiveScreens

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: BookHiveScreens
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val items = listOf(
            BottomNavItem(
                title = "Add",
                icon = Icons.Rounded.AddCircleOutline,
                route = BookHiveScreens.AddBookScreen
            ),
            BottomNavItem(
                title = "Home",
                icon = Icons.Rounded.Home,
                route = BookHiveScreens.HomeScreen
            ),
            BottomNavItem(
                title = "Me",
                icon = Icons.Rounded.Person,
                route = BookHiveScreens.MeScreen
            )
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    val route = if (item.argument != null) {
                        item.route.name + item.argument
                    } else {
                        item.route.name
                    }
                    navController.navigate(route)
                },
                icon = {
                    NavigationItemContent(
                        title = item.title,
                        icon = item.icon,
                        selected = currentRoute == item.route,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

@SuppressLint("InvalidColorHexValue")
@Composable
private fun NavigationItemContent(
    title: String,
    icon: ImageVector,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$title Icon",
            tint = if (selected) {
                MaterialTheme.colorScheme.secondary
            } else {
                Color(0xFF3218790)
            }
        )
        Text(
            text = title,
//            modifier = Modifier.size(if (title == "Home") 36.dp else 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.secondary
            } else {
                Color(0xFF3218790)
            }
        )
    }
}

private data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: BookHiveScreens,
    val argument: String? = null
)