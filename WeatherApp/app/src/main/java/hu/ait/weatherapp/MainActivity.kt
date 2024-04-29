package hu.ait.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.weatherapp.ui.navigation.MainNavigation
import hu.ait.weatherapp.ui.screen.CitiesScreen
import hu.ait.weatherapp.ui.screen.SplashScreen
import hu.ait.weatherapp.ui.screen.WeatherScreen
import hu.ait.weatherapp.ui.theme.WeatherAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherNavHost()

                }
            }
        }
    }
}

@Composable
fun WeatherNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainNavigation.SplashScreen.route
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(MainNavigation.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(MainNavigation.CitiesScreen.route) {
            CitiesScreen(
                onNavigateToWeather = { city ->
                    navController.navigate(
                        MainNavigation.WeatherScreen.createRoute(city)
                    )
                }
            )
        }
        composable(MainNavigation.WeatherScreen.route, arguments = listOf( navArgument("city") {type = NavType.StringType} )) {
            val city = it.arguments?.getString("city")
            WeatherScreen(cityName = city)
        }
    }
}