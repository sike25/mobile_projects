package hu.ait.weatherapp.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CitiesViewModel : ViewModel() {

    private var citiesList = mutableStateListOf("Boston", "Budapest", "Blah-dee-dah")

    fun getCitiesList(): MutableList<String> {
        return citiesList
    }

    fun addToCitiesList(newCity: String) {
        citiesList.add(newCity)
    }

    fun removeFromCitiesList(toRemove: String) {
        citiesList.remove(toRemove)
    }
}