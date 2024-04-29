package hu.ait.weatherapp.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.weatherapp.data.WeatherResult
import hu.ait.weatherapp.network.WeatherAPI
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private var weatherAPI: WeatherAPI
) : ViewModel() {

    var weatherUIState: WeatherUiState by mutableStateOf(WeatherUiState.Init)

    fun getWeatherResults (q: String, appid: String, units: String) {
        weatherUIState = WeatherUiState.Loading

        viewModelScope.launch {
            weatherUIState = try {
                val result = weatherAPI.getWeatherResults(q, units, appid)

                WeatherUiState.Success(result)
            } catch (e: Exception) {
                WeatherUiState.Error(e.message!!)
            }
        }
    }
}

sealed interface WeatherUiState {
    data object Init : WeatherUiState
    data object Loading : WeatherUiState
    data class Success (val weatherResult: WeatherResult) : WeatherUiState
    data class Error(val errorMsg: String) : WeatherUiState
}