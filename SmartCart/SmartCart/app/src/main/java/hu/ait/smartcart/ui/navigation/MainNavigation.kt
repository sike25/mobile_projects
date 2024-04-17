package hu.ait.smartcart.ui.navigation

sealed class MainNavigation(val route: String) {
    data object SplashScreen : MainNavigation("splashscreen")
    data object ShoppingListScreen: MainNavigation("shoppinglistscreen")
    data object SummaryScreen: MainNavigation("summaryscreen")
}