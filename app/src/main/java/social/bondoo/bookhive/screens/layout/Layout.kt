package social.bondoo.bookhive.screens.layout

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import social.bondoo.bookhive.navigation.BookHiveScreens

@Composable
fun Layout(
    navController: NavController? = null,
    title: String? = null,
    showBottomBar: Boolean? = true,
    showIcon: Boolean = false,
    showTopBar: Boolean = true,
    topBarType: TopBarType = TopBarType.HomeScreen,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (topBarType == TopBarType.HomeScreen) {
                FloatingActionButton(
                    onClick = { navController?.navigate(BookHiveScreens.AddBookScreen.name) },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Book",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Start,
        bottomBar = {
            if (showBottomBar == true) {
                val currentBackStackEntry = navController?.currentBackStackEntryAsState()?.value
                val currentRoute =
                    BookHiveScreens.fromRoute(currentBackStackEntry?.destination?.route)
                navController?.let {
                    BottomBar(
                        navController = it,
                        currentRoute = currentRoute
                    )
                }
            }
        },
        topBar = {
            if (showTopBar) {
                TopBarContent(
                    topBarType = topBarType,
                    title = title ?: "",
                    navController = navController,
                    showIcon = showIcon
                )
            }
        }
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current
        Surface(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                },
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}