package com.grupo12.guiaviajespersonalizada.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo12.guiaviajespersonalizada.data.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _popularDestinations = MutableLiveData<List<PopularDestination>>()
    val popularDestinations: LiveData<List<PopularDestination>> = _popularDestinations

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 🔹 Cargar destinos con imágenes reales desde Unsplash
    fun loadPopularDestinations() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val destinationsData = listOf(
                    mapOf(
                        "id" to 1,
                        "name" to "París",
                        "country" to "Francia",
                        "query" to "paris eiffel tower",
                        "price" to "$599",
                        "rating" to 4.8f,
                        "description" to "La ciudad del amor con la Torre Eiffel y el Louvre"
                    ),
                    mapOf(
                        "id" to 2,
                        "name" to "Tokio",
                        "country" to "Japón",
                        "query" to "tokyo japan",
                        "price" to "$799",
                        "rating" to 4.9f,
                        "description" to "Metrópolis moderna con tradición ancestral"
                    ),
                    mapOf(
                        "id" to 3,
                        "name" to "Nueva York",
                        "country" to "Estados Unidos",
                        "query" to "new york city",
                        "price" to "$699",
                        "rating" to 4.7f,
                        "description" to "La gran manzana que nunca duerme"
                    ),
                    mapOf(
                        "id" to 4,
                        "name" to "Londres",
                        "country" to "Reino Unido",
                        "query" to "london big ben",
                        "price" to "$649",
                        "rating" to 4.6f,
                        "description" to "Historia, cultura y el Big Ben"
                    ),
                    mapOf(
                        "id" to 5,
                        "name" to "Barcelona",
                        "country" to "España",
                        "query" to "barcelona sagrada familia",
                        "price" to "$549",
                        "rating" to 4.8f,
                        "description" to "Arquitectura de Gaudí y playas mediterráneas"
                    ),
                    mapOf(
                        "id" to 6,
                        "name" to "Machu Picchu",
                        "country" to "Perú",
                        "query" to "machu picchu peru",
                        "price" to "$899",
                        "rating" to 4.9f,
                        "description" to "Ciudadela inca en los Andes"
                    )
                )

                val destinations = mutableListOf<PopularDestination>()

                for (data in destinationsData) {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            ApiClient.unsplashApi.searchPhotos(
                                query = data["query"] as String,
                                perPage = 1,
                                clientId = ApiClient.getAccessKey()
                            )
                        }

                        val imageUrl = response.results.firstOrNull()?.urls?.regular
                            ?: "https://source.unsplash.com/featured/?${data["query"]}"

                        val destination = PopularDestination(
                            id = data["id"] as Int,
                            name = data["name"] as String,
                            country = data["country"] as String,
                            imageRes = imageUrl,
                            price = data["price"] as String,
                            rating = data["rating"] as Float,
                            description = data["description"] as String
                        )

                        destinations.add(destination)
                        println("✅ Imagen cargada: ${data["name"]} -> $imageUrl")

                    } catch (e: Exception) {
                        println("❌ Error con ${data["name"]}: ${e.message}")
                        destinations.add(
                            PopularDestination(
                                id = data["id"] as Int,
                                name = data["name"] as String,
                                country = data["country"] as String,
                                imageRes = "https://source.unsplash.com/featured/?${data["query"]}",
                                price = data["price"] as String,
                                rating = data["rating"] as Float,
                                description = data["description"] as String
                            )
                        )
                    }
                }

                _popularDestinations.value = destinations

            } catch (e: Exception) {
                println("❌ Error general al cargar destinos: ${e.message}")
                e.printStackTrace()
                loadFallbackDestinations()
            }

            _isLoading.value = false
        }
    }

    // 🔸 Método de respaldo si falla la API
    private fun loadFallbackDestinations() {
        val destinations = listOf(
            PopularDestination(1, "París", "Francia", "https://source.unsplash.com/featured/?paris", "$599", 4.8f, "La ciudad del amor con la Torre Eiffel y el Louvre"),
            PopularDestination(2, "Tokio", "Japón", "https://source.unsplash.com/featured/?tokyo", "$799", 4.9f, "Metrópolis moderna con tradición ancestral"),
            PopularDestination(3, "Nueva York", "EE.UU.", "https://source.unsplash.com/featured/?newyork", "$699", 4.7f, "La gran manzana que nunca duerme"),
            PopularDestination(4, "Londres", "Reino Unido", "https://source.unsplash.com/featured/?london", "$649", 4.6f, "Historia, cultura y el Big Ben"),
            PopularDestination(5, "Barcelona", "España", "https://source.unsplash.com/featured/?barcelona", "$549", 4.8f, "Arquitectura de Gaudí y playas mediterráneas"),
            PopularDestination(6, "Machu Picchu", "Perú", "https://source.unsplash.com/featured/?machupicchu", "$899", 4.9f, "Ciudadela inca en los Andes")
        )
        _popularDestinations.value = destinations
    }

    // ✅ 🔹 Búsqueda con conexión a Unsplash API (versión que agrega la primera card dinámica)
    fun searchDestinations(destination: String, date: String, travelers: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.unsplashApi.searchPhotos(
                        query = destination,
                        perPage = 1,
                        clientId = ApiClient.getAccessKey()
                    )
                }

                val photoUrl = response.results.firstOrNull()?.urls?.regular
                    ?: "https://source.unsplash.com/featured/?$destination"

                // Crear un destino dinámico que se mostrará como primera card
                val searchResultAsDestination = PopularDestination(
                    id = 999,
                    name = destination,
                    country = "Resultado de búsqueda",
                    imageRes = photoUrl,
                    price = "$${(600..1200).random()}",
                    rating = (42..49).random() / 10.0f,
                    description = "Paquete personalizado para $travelers ${if (travelers == 1) "viajero" else "viajeros"} - Fecha: $date"
                )

                // Insertar el nuevo resultado al inicio de la lista
                val currentList = _popularDestinations.value?.toMutableList() ?: mutableListOf()
                currentList.removeAll { it.id == 999 } // elimina anteriores búsquedas
                currentList.add(0, searchResultAsDestination)

                _popularDestinations.value = currentList
                println("✅ Resultado de búsqueda agregado: $destination -> $photoUrl")

            } catch (e: Exception) {
                e.printStackTrace()
                println("❌ Error al obtener imagen de Unsplash: ${e.message}")
            }

            _isLoading.value = false
        }
    }
}
