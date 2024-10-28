package social.bondoo.bookhive.screens.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import social.bondoo.bookhive.R

sealed class TopBarType {
    data object HomeScreen : TopBarType()
    data object UpdateScreen : TopBarType()
    data object AddBookScreen : TopBarType()
    data object MeScreen : TopBarType()
    data object SplashScreen : TopBarType()
    data object LoginScreen : TopBarType()
    data object DetailsScreen : TopBarType()
}

@Composable
fun TopBarContent(
    topBarType: TopBarType,
    title: String = "",
    showIcon: Boolean = false,
    navController: NavController? = null,
    actions: (@Composable () -> Unit)? = null
) {
    TopBar(
        topBarType = topBarType,
        title = title,
        navigationIconSlot = {
            if (showIcon) when (topBarType) {
                TopBarType.HomeScreen -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.library),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(25.dp)
                    )
                }

                else -> {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(25.dp)
                            .clip(shape = CircleShape)
                            .clickable { navController?.popBackStack() },
                    )
                }
            }
        },
        actionSlot = actions
    )
}
