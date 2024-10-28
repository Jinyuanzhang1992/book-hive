package social.bondoo.bookhive.screens.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    topBarType: TopBarType,
    title: String = "",
    navigationIconSlot: (@Composable () -> Unit)? = null,
    actionSlot: (@Composable () -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        navigationIcon = {
            navigationIconSlot?.invoke()
        },
        title = {
            when (topBarType) {
                TopBarType.HomeScreen -> Modifier.fillMaxWidth()
                else -> Modifier
            }.let {
                Box(
                    modifier  = it,
                    contentAlignment = when (topBarType) {
                        TopBarType.HomeScreen -> Alignment.TopStart
                        else -> Alignment.Center
                    }
                ) {
                    if (topBarType == TopBarType.HomeScreen) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    } else {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        actions = {
            actionSlot?.invoke()
        }
    )
}