package social.bondoo.bookhive.navigation

enum class BookHiveScreens {
    HomeScreen,
    AddBookScreen,
    MeScreen,
    LoginScreen,
    RegisterScreen,
    UpdateBookScreen,
    DetailScreen,
    SplashScreen;

    companion object {
        fun fromRoute(route:String?): BookHiveScreens
                = when (route?.substringBefore("/")) {
            HomeScreen.name -> HomeScreen
            SplashScreen.name -> SplashScreen
            AddBookScreen.name -> AddBookScreen
            MeScreen.name -> MeScreen
            LoginScreen.name -> LoginScreen
            RegisterScreen.name -> RegisterScreen
            UpdateBookScreen.name -> UpdateBookScreen
            DetailScreen.name -> DetailScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized.")
        }
    }
}