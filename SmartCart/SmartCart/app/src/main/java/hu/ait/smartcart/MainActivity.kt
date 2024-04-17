package hu.ait.smartcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.smartcart.ui.navigation.MainNavigation
import hu.ait.smartcart.ui.screen.ShoppingListScreen
import hu.ait.smartcart.ui.screen.SplashScreen
import hu.ait.smartcart.ui.screen.SummaryScreen
import hu.ait.smartcart.ui.theme.SmartCartTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCartTheme {
                Surface( modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ShopNavHost()
                }
            }
        }
    }
}


@Composable
fun ShopNavHost(
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
        composable (MainNavigation.ShoppingListScreen.route) {
            ShoppingListScreen(
                onNavigateToSummary = {
                    navController.navigate(
                        MainNavigation.SummaryScreen.route)
                }
            )
        }
        composable(MainNavigation.SummaryScreen.route) {
            SummaryScreen()
        }
    }
}

