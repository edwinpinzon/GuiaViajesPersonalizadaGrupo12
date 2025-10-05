package com.grupo12.guiaviajespersonalizada.data.api

// Representa una sola foto devuelta por Unsplash
data class UnsplashPhoto(
    val id: String,
    val description: String?, // puede ser nulo
    val urls: Urls // contiene los enlaces de la imagen
)

// Representa los distintos tamaños de imagen
data class Urls(
    val small: String,   // imagen pequeña
    val regular: String  // imagen de tamaño normal
)

// Representa la respuesta general cuando se hace una búsqueda
data class UnsplashSearchResponse(
    val results: List<UnsplashPhoto> // lista de fotos
)
