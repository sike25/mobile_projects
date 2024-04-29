package hu.ait.weatherapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.weatherapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onNavigateToWeather: (String) -> Unit,
    citiesViewModel: CitiesViewModel = viewModel()
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    Box {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFB996EC)),
                    actions = {}
                )
            },

            content = { paddingValues ->
                Column (modifier = Modifier.padding(paddingValues)) {
                    CitiesContent(citiesViewModel = citiesViewModel, onNavigateToWeather = onNavigateToWeather)

                    if (showAddDialog) {
                        AddNewCitiesDialog(
                            citiesViewModel = citiesViewModel,
                            onDismissRequest = { showAddDialog = false }
                        )
                    }
                }
            }
        )

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFFB996EC),
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            ) {
            Icon(Icons.Filled.Add, stringResource(R.string.desc_add_to_cities_list))
        }
    }
}

@Composable
fun CitiesContent(
    citiesViewModel: CitiesViewModel,
    onNavigateToWeather: (String) -> Unit,
) {

    if (citiesViewModel.getCitiesList().isNotEmpty()) {
        LazyColumn( modifier = Modifier.fillMaxWidth() ) {
            items(citiesViewModel.getCitiesList()) {
                CityCard(city = it, onNavigateToWeather = onNavigateToWeather, onRemoveItem = { citiesViewModel.removeFromCitiesList(it) },
                )
            }
        }
    }
}

@Composable
fun CityCard(
    city: String,
    onNavigateToWeather: (String) -> Unit,
    onRemoveItem: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation( defaultElevation = 5.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onNavigateToWeather( city ) }
    ) {
        Row( modifier = Modifier.padding(20.dp)) {
            Text( text = city, modifier = Modifier.weight(1f) )
            Icon( Icons.Filled.Clear, stringResource(R.string.desc_remove_city), modifier = Modifier.clickable {
                onRemoveItem()
            })
        }
    }

}

@Composable
fun AddNewCitiesDialog(
    citiesViewModel: CitiesViewModel,
    onDismissRequest: () -> Unit,
) {
    var city by rememberSaveable { mutableStateOf("") }
    var cityError by rememberSaveable { mutableStateOf(false) }
    fun validateCity(text: String) { cityError = text == "" }

    Dialog (
        onDismissRequest = { onDismissRequest() }
    ) {
        Card ( modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
        ) {
            Column( Modifier.padding(16.dp) ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {
                        city = it
                        validateCity(it)
                    },
                    label = { Text(text = stringResource(R.string.text_enter_city)) },
                    isError = cityError,
                    trailingIcon = {
                        if (cityError) {
                            Icon( Icons.Filled.Warning, stringResource(R.string.desc_error), tint = MaterialTheme.colorScheme.error)
                        }
                    },
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            validateCity(city)
                            if (!cityError) {
                                citiesViewModel.addToCitiesList(city)
                                onDismissRequest()
                            }
                        },
                    ) {
                        Text(text = stringResource(R.string.text_save))
                    }

                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = stringResource(R.string.text_cancel))
                    }
                }
            }
        }
    }
}
