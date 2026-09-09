package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listycity.ui.theme.ListyCityTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Add City Repository variable for main screen to access it
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        // Pass lambda (The method); it refers to the city itself
                        onDeleteCity = { cityRepository.deleteCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

class CityRepository {
    // The state can change on time; add/delete more cities
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    // Getter function that gives you the list of cities
    val cities: List<String> get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }

}

// Composable so it knows it will be used to show some contents
@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit, // Arg to add functionality to make buttons work
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Make a Column UI Element
    // .fillMaxSize(): This column we're creating will fill the whole screen
    var newCityName by remember {mutableStateOf("")} // Cache in New City
    // Add functionality to delete city as well

    Column(modifier = modifier.fillMaxSize()) {
        // Add Two Buttons to add or remove cities
        // mutableStateOf(""): Cache it as an empty string
        Row(modifier = Modifier.padding( all = 16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City Name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Button that Adds New Cities
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }

            // Button that Deletes Cities
            Button (
                onClick = {
                    if (newCityName in cities) {
                        onDeleteCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }



        LazyColumn(modifier = modifier.fillMaxSize()) {
            // For loop (Lambda) that displays every city
            items(cities) { city ->
                CityRow(city = city)
            }
        }
    }

}

@Composable
fun CityRow(city: String) {
    // Show text
    Text(
        text = city, // What words to display
        fontSize = 28.sp, // Set font (.sp is required and has to be imported)
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)
        // .fillMaxWidth: Make it occupy the whole width of the screen
        // .padding: Add padding in horizontal and vertical
        // Red text => Error in called method/function
    )
}