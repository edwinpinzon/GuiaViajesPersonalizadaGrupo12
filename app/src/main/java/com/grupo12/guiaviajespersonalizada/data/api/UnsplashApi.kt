    package com.grupo12.guiaviajespersonalizada.data.api

    import retrofit2.http.GET
    import retrofit2.http.Query

    /**
     * Interfaz que define los endpoints de la API de Unsplash
     * Retrofit genera la implementación automáticamente.
     */
    interface UnsplashApi {

        // Endpoint para buscar fotos según una palabra clave (por ejemplo: "Paris", "Tokyo", etc.)
        @GET("search/photos")
        suspend fun searchPhotos(
            @Query("query") query: String,            // Término de búsqueda
            @Query("per_page") perPage: Int = 1,      // Número de imágenes a obtener
            @Query("client_id") clientId: String      // Tu Access Key de Unsplash
        ): UnsplashSearchResponse
    }
