package hu.ait.weatherapp.ui.navigation

sealed class MainNavigation(val route: String) {
    data object SplashScreen : MainNavigation("splash_screen")
    data object CitiesScreen : MainNavigation("cities_screen")
    data object WeatherScreen: MainNavigation("weather_screen?city={city}") {
        fun createRoute(city: String) : String {
            return "weather_screen?city=$city"
        }
    }
}
