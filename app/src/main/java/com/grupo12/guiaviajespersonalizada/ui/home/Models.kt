package com.grupo12.guiaviajespersonalizada.ui.home

// Modelo para destinos populares
data class PopularDestination(
    val id: Int,
    val name: String,
    val country: String,
    val imageRes: String, // Nombre del recurso drawable
    val price: String,
    val rating: Float,
    val description: String,
    val isFavorite: Boolean = false
)

// Modelo para resultados de búsqueda
data class SearchResult(
    val destination: String,
    val hotel: String,
    val flightPrice: String,
    val hotelPrice: String,
    val totalPrice: String,
    val rating: Float
)