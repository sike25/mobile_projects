package hu.ait.weatherapp.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.ait.weatherapp.R
import hu.ait.weatherapp.data.WeatherResult

@Composable
fun WeatherScreen(
    cityName: String?,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        cityName?.let {
            weatherViewModel.getWeatherResults(cityName, "f08d5957a1656be3c92294304559c69c", "imperial")
        }
    }

    Column {
        when (weatherViewModel.weatherUIState) {
            is WeatherUiState.Init -> {}
            is WeatherUiState.Loading -> WeatherFetching()
            is WeatherUiState.Error -> WeatherError(( weatherViewModel.weatherUIState as WeatherUiState.Error ).errorMsg)
            is WeatherUiState.Success -> WeatherContentB( ( weatherViewModel.weatherUIState as WeatherUiState.Success ).weatherResult )
        }
    }
}


@Composable
fun WeatherContentB(
    weatherResult: WeatherResult
) {

    // Weather Details
    val city = weatherResult.name
    val country = weatherResult.sys.country
    val latitude = weatherResult.coord.lat
    val longitude = weatherResult.coord.lon
    val description = weatherResult.weather[0].description
    val icon = weatherResult.weather[0].icon
    val tempFeelsLike = weatherResult.main.feelsLike
    val tempMin = weatherResult.main.tempMin
    val tempMax = weatherResult.main.tempMax
    val humidity = weatherResult.main.humidity
    val pressure = weatherResult.main.pressure
    val windSpeed = weatherResult.wind.speed

    // Map Settings and Properties
    val zoom = 10f
    val uiSettings by remember { mutableStateOf ( MapUiSettings ( zoomControlsEnabled = true, zoomGesturesEnabled = true ) ) }
    val mapProperties by remember { mutableStateOf ( MapProperties ( mapType = MapType.NORMAL ) ) }
    val cameraState = rememberCameraPositionState{ CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), zoom) }

    LaunchedEffect(cameraState) {
        cameraState.move(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), zoom)))
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFB996EC),
                contentColor = Color.Black
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .border(5.dp, Color.Black)
                        .padding(10.dp),
                    uiSettings = uiSettings,
                    properties = mapProperties,
                    cameraPositionState = cameraState
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(latitude, longitude)),
                        title = city,
                        snippet = city,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "$city, $country",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://openweathermap.org/img/w/${icon}.png")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.desc_weather_icon),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between image and text
                            Text(
                                text = description.replaceFirstChar { it.uppercase() }, // Capitalize the first letter
                                style = MaterialTheme.typography.bodyLarge,
                             )
                        }
                    }
                }

            }

        }


        Spacer(modifier = Modifier.height(20.dp))


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFB996EC),
                contentColor = Color.Black
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                WeatherDetailRow("Feels like", formatTemp(tempFeelsLike.toFloat()))
                WeatherDetailRow("Temperature", "${formatTemp(tempMin.toFloat())} to ${formatTemp(tempMax.toFloat())}")
                WeatherDetailRow("Humidity", "$humidity%")
                WeatherDetailRow("Pressure", "$pressure hPa")
                WeatherDetailRow("Wind Speed", "${formatSpeed(windSpeed.toFloat())} m/s")
            }
        }
    }
}


@Composable
fun WeatherDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

fun formatTemp(temp: Float): String = "%.1f°F".format(temp)
fun formatSpeed(speed: Float): String = "%.2f".format(speed)



@Composable
fun WeatherFetching() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.text_fetching_data))
        }
    }
}

@Composable
fun WeatherError(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.7f)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(id = R.string.desc_error),
                    tint = Color.Red,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.desc_error),
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }

        }
    }
}



