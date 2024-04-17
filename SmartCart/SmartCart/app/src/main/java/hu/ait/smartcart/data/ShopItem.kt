package hu.ait.smartcart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.smartcart.R
import java.io.Serializable

@Entity(tableName = "shoptable")
data class ShopItem (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "category") val category: ShopItemCategory,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "bought") var bought: Boolean
) : Serializable

enum class ShopItemCategory {
    FOOD, FASHION, TECH;
    fun getIcon():Int {
        return if (this == FOOD) {
            R.drawable.food
        } else if (this == FASHION) {
            R.drawable.fashion
        } else if (this == TECH) {
            R.drawable.tech
        } else {
            0
        }
    }
}

class ShopItemCategoryList {
    private val categoryList: List<String> =
        listOf( ShopItemCategory.FOOD.toString(),
                ShopItemCategory.FASHION.toString(),
                ShopItemCategory.TECH.toString()
        )
    fun getCategoryList(): List<String> {
        return categoryList
    }
}

/***
 * Icons:
<a href="https://www.flaticon.com/free-icons/food" title="food icons">Food icons created by Freepik - Flaticon</a>
<a href="https://www.flaticon.com/free-icons/fashion" title="fashion icons">Fashion icons created by Freepik - Flaticon</a>
<a href="https://www.flaticon.com/free-icons/technology" title="technology icons">Technology icons created by Freepik - Flaticon</a>
 ***/


