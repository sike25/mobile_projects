package hu.ait.smartcart.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.smartcart.R
import hu.ait.smartcart.data.ShopItemCategory
import hu.ait.smartcart.model.ShopViewModel
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    shopViewModel: ShopViewModel = hiltViewModel()
) {
    val boughtItemsPrice by shopViewModel.getTotalBoughtItemsPrice().collectAsState(initial = 0)
    val foodItems by shopViewModel.getTotalCategoryItems(ShopItemCategory.FOOD).collectAsState(initial = 0)
    val fashionItems by shopViewModel.getTotalCategoryItems(ShopItemCategory.FASHION).collectAsState(initial = 0)
    val techItems by shopViewModel.getTotalCategoryItems(ShopItemCategory.TECH).collectAsState(initial = 0)

    val limit = max(techItems, max(foodItems, fashionItems))

    val toShow = boughtItemsPrice ?: 0.0

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text(text = stringResource(R.string.text_summary_statistics))},
                colors = TopAppBarDefaults.topAppBarColors( containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {}
            )
        },

        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Column (modifier = Modifier.padding(20.dp)) {
                    HistogramCategoryItem(categoryName = stringResource(R.string.desc_food), count = foodItems, color = Color(0xFF4CAF50), limit = limit)
                    HistogramCategoryItem(categoryName = stringResource(R.string.desc_fashion), count = fashionItems, color = Color(0xFF9C27B0), limit = limit)
                    HistogramCategoryItem(categoryName = stringResource(R.string.desc_tech), count = techItems, color = Color(0xFF2196F3), limit = limit)

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(text = stringResource(R.string.total_money_spent), fontSize = 20.sp, modifier = Modifier.padding(10.dp))

                    Text(text = "$toShow", modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}

@Composable
fun HistogramCategoryItem(categoryName: String, count: Int, color: Color, limit: Int) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(text = "$categoryName Items: $count", style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = count.toFloat() / (limit + 1).toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            color = color
        )
    }
}


