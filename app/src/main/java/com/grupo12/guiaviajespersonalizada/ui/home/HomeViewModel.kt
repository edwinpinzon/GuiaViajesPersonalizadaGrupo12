package com.grupo12.guiaviajespersonalizada.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _popularDestinations = MutableLiveData<List<PopularDestination>>()
    val popularDestinations: LiveData<List<PopularDestination>> = _popularDestinations

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadPopularDestinations() {
        viewModelScope.launch {
            _isLoading.value = true

            // Simular carga de datos
            delay(1000)

            val destinations = listOf(
                PopularDestination(
                    id = 1,
                    name = "París",
                    country = "Francia",
                    imageRes = "paris_destination", // Nombre sin extensión
                    price = "$599",
                    rating = 4.8f,
                    description = "La ciudad del amor con la Torre Eiffel y el Louvre"
                ),
                PopularDestination(
                    id = 2,
                    name = "Tokio",
                    country = "Japón",
                    imageRes = "tokyo_destination",
                    price = "$799",
                    rating = 4.9f,
                    description = "Metrópolis moderna con tradición ancestral"
                ),
                PopularDestination(
                    id = 3,
                    name = "Nueva York",
                    country = "Estados Unidos",
                    imageRes = "newyork_destination",
                    price = "$699",
                    rating = 4.7f,
                    description = "La gran manzana que nunca duerme"
                ),
                PopularDestination(
                    id = 4,
                    name = "Londres",
                    country = "Reino Unido",
                    imageRes = "london_destination",
                    price = "$649",
                    rating = 4.6f,
                    description = "Historia, cultura y el Big Ben"
                ),
                PopularDestination(
                    id = 5,
                    name = "Barcelona",
                    country = "España",
                    imageRes = "beach_destination",
                    price = "$549",
                    rating = 4.8f,
                    description = "Arquitectura de Gaudí y playas mediterráneas"
                ),
                PopularDestination(
                    id = 6,
                    name = "Machu Picchu",
                    country = "Perú",
                    imageRes = "mountain_destination",
                    price = "$899",
                    rating = 4.9f,
                    description = "Ciudadela inca en los Andes"
                )
            )

            _popularDestinations.value = destinations
            _isLoading.value = false
        }
    }

    fun searchDestinations(destination: String, date: String, travelers: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simular búsqueda
            delay(1500)

            val results = listOf(
                SearchResult(
                    destination = destination,
                    hotel = "Hotel Premium $destination",
                    flightPrice = "${(400..800).random()}",
                    hotelPrice = "${(200..500).random()}",
                    totalPrice = "${(800..1500).random()}",
                    rating = (40..50).random() / 10.0f
                ),
                SearchResult(
                    destination = destination,
                    hotel = "Resort $destination",
                    flightPrice = "${(300..700).random()}",
                    hotelPrice = "${(150..400).random()}",
                    totalPrice = "${(600..1200).random()}",
                    rating = (35..48).random() / 10.0f
                )
            )

            _searchResults.value = results
            _isLoading.value = false
        }
    }
}