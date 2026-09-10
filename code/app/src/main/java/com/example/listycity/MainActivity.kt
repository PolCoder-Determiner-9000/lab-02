package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.graphics.Color

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
    var deleteCityName by remember {mutableStateOf("")} // Cache in city to be deleted
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
                    if (deleteCityName in cities) {
                        onDeleteCity(deleteCityName)
                        deleteCityName = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            // For loop (Lambda) that displays every city
            items(cities) { city ->
                CityRow(city = city,
                    deleteCityName = deleteCityName,
                    // Lambda function: Pass String and assign (returns) it to variable
                    onCitySelected = { deleteCityName = it }
                )
            }
        }
    }

}

/*
Citations:
   - Selection functionality was based on the answer nglauber gave from
   stackoverflow.
   Author: https://stackoverflow.com/users/1094333/nglauber
   Question: "How to select only one item in a list (LazyColumn)?"
   https://stackoverflow.com/questions/72531840/how-to-select-only-one-item-in-a-list-lazycolumn
   Answer:
   https://stackoverflow.com/a/72537011
   Date: Jun 7, 2022
   License: CC BY-SA
   - Additional help turning that answer into one where I can use to manipulate deleteCityName is
   provided by Claude (Alongside help with citations)
        Conversation: "How do I change the value of deleteCityName?"
        Date: Sept 10, 2026
        Model: Claude Sonnet 5
        Link: https://claude.ai/share/15407b24-690f-4baf-9fb5-2bc66f710bb6
*/
@Composable
fun CityRow(city: String, deleteCityName: String, onCitySelected: (String) -> Unit) {
    // Show text
    Text(
        text = city, // What words to display
        fontSize = 28.sp, // Set font (.sp is required and has to be imported)
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)
            // Add selection functionality to
            .selectable(
                selected = deleteCityName == city,
                onClick = {
                    onCitySelected(city)
                }
            )
            .background(
                if (deleteCityName == city) Color.Gray
                else Color.Transparent
            )
            .padding(8.dp)
        // .fillMaxWidth: Make it occupy the whole width of the screen
        // .padding: Add padding in horizontal and vertical
        // Red text => Error in called method/function
    )
}