package hu.ait.smartcart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDAO {
    @Query("SELECT * from shoptable")
    fun getAllShopItems(): Flow<List<ShopItem>>

    @Query("SELECT * from shoptable WHERE id = :id")
    fun getShopItem(id: Int): Flow<ShopItem>

    @Query("SELECT COUNT(*) from shoptable")
    suspend fun getShopItemNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shopItem: ShopItem)

    @Update
    suspend fun update(shopItem: ShopItem)

    @Delete
    suspend fun delete(shopItem: ShopItem)

    @Query("DELETE from shoptable")
    suspend fun deleteAllShopItems()

    @Query("SELECT * FROM shoptable ORDER BY name")
    fun getAllShopItemsSortedByName(): Flow<List<ShopItem>>

    @Query("SELECT * FROM shoptable ORDER BY price")
    fun getAllShopItemsSortedByPrice(): Flow<List<ShopItem>>

    @Query("SELECT SUM(price) FROM shoptable WHERE bought = 1")
    fun getTotalBoughtItemsPrice(): Flow<Double>

    @Query("SELECT COUNT(*) FROM shoptable WHERE category = :category")
    fun getTotalCategoryItems(category: ShopItemCategory): Flow<Int>
}
