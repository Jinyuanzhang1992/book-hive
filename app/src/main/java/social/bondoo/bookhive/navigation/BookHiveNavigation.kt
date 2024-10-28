package social.bondoo.bookhive.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import social.bondoo.bookhive.screens.addBook.AddBookScreen
import social.bondoo.bookhive.screens.addBook.AddBookViewModel
import social.bondoo.bookhive.screens.details.DetailsScreen
import social.bondoo.bookhive.screens.details.DetailsScreenViewModel
import social.bondoo.bookhive.screens.home.HomeScreen
import social.bondoo.bookhive.screens.home.HomeScreenViewModel
import social.bondoo.bookhive.screens.login.LoginScreen
import social.bondoo.bookhive.screens.login.LoginScreenViewModel
import social.bondoo.bookhive.screens.me.MeScreen
import social.bondoo.bookhive.screens.splash.SplashScreen
import social.bondoo.bookhive.screens.updateBook.UpdateBookScreen

@Composable
fun BookHiveNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BookHiveScreens.SplashScreen.name
    ) {
        //Splash Screen
        composable(BookHiveScreens.SplashScreen.name) {
//            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SplashScreen(navController = navController)
        }

        //Home Screen
        val route = BookHiveScreens.HomeScreen.name
        composable(route) {
            val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, homeScreenViewModel = homeScreenViewModel)
        }

        //Me Screen
        composable(BookHiveScreens.MeScreen.name) {
            val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
            MeScreen(navController = navController, homeScreenViewModel = homeScreenViewModel)
        }

        //Add Book Screen
        composable(BookHiveScreens.AddBookScreen.name) {
            val addBookViewModel= hiltViewModel<AddBookViewModel>()
            AddBookScreen(navController = navController, addBookViewModel = addBookViewModel)
        }

        //Update Book Screen
        composable(BookHiveScreens.UpdateBookScreen.name+"/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType })
            ) { backStackEntry ->
            val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
            backStackEntry.arguments?.getString("bookItemId")?.let { bookItemId ->
                UpdateBookScreen(navController = navController, bookItemId = bookItemId, homeScreenViewModel = homeScreenViewModel)
            }
        }

        //login screen
        composable(BookHiveScreens.LoginScreen.name) {
            val loginScreenViewModel: LoginScreenViewModel = viewModel()
            LoginScreen(navController = navController, loginScreenViewModel = loginScreenViewModel)
        }

        //Detail Screen
        composable(BookHiveScreens.DetailScreen.name+ "/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })
            ) { backStackEntry ->
            val detailsScreenViewModel = hiltViewModel<DetailsScreenViewModel>()
            backStackEntry.arguments?.getString("bookId")?.let { bookId ->
                DetailsScreen(
                    navController = navController,
                    bookId = bookId,
                    detailsScreenViewModel = detailsScreenViewModel,
                )
            }
        }
    }
}