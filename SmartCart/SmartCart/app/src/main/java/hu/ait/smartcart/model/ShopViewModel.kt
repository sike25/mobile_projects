package hu.ait.smartcart.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.smartcart.data.ShopDAO
import hu.ait.smartcart.data.ShopItem
import hu.ait.smartcart.data.ShopItemCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopDAO: ShopDAO
) : ViewModel() {

    private val _shopItems = MutableStateFlow<List<ShopItem>>(emptyList())
    val shopItems: StateFlow<List<ShopItem>> = _shopItems.asStateFlow()

    init {
        loadShopItems()
    }

    private fun loadShopItems() {
        viewModelScope.launch {
            shopDAO.getAllShopItems().collect {
                _shopItems.value = it
            }
        }
    }

    fun addToShopList(shopItem: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopDAO.insert(shopItem)
        }
    }

    fun removeFromShopList(shopItem: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopDAO.delete(shopItem)
        }
    }

    fun editShopItem(editedShopItem: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopDAO.update(editedShopItem)
        }
    }

    fun changeShopItemBoughtState(shopItem: ShopItem, value:Boolean) {
        val updatedShopItem = shopItem.copy()
        updatedShopItem.bought = value
        viewModelScope.launch {
            shopDAO.update(updatedShopItem)
        }
    }

    fun clearShopList() {
        viewModelScope.launch(Dispatchers.IO) {
            shopDAO.deleteAllShopItems()
        }
    }

    fun sortShopListByName() {
        viewModelScope.launch {
            shopDAO.getAllShopItemsSortedByName().collect {
                _shopItems.value = it
            }
        }
    }

    fun sortShopListByPrice() {
        viewModelScope.launch {
            shopDAO.getAllShopItemsSortedByPrice().collect {
                _shopItems.value = it
            }
        }
    }

    fun getTotalBoughtItemsPrice(): Flow<Double> {
        return shopDAO.getTotalBoughtItemsPrice()
    }

    fun getTotalCategoryItems(category: ShopItemCategory): Flow<Int> {
        return shopDAO.getTotalCategoryItems(category)
    }
}