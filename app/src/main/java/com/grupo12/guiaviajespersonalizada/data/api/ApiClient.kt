package com.grupo12.guiaviajespersonalizada.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Cliente de Retrofit que gestiona la conexión con la API de Unsplash.
 * Aquí se configura la URL base, la conversión JSON y el cliente HTTP.
 */
object ApiClient {

    // URL base de la API de Unsplash
    private const val BASE_URL = "https://api.unsplash.com/"

    // Clave pública de acceso (Access Key)
    private const val ACCESS_KEY = "LcAe_A6i0th3dJ9x2Ai91DIBEEefBBND46KQVE1EPLU"

    // Interceptor para ver logs de las peticiones HTTP (útil para depuración)
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configuración del cliente HTTP
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Configuración de Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Kotlin
        .client(httpClient)
        .build()

    // Instancia de la API de Unsplash
    val unsplashApi: UnsplashApi = retrofit.create(UnsplashApi::class.java)

    /**
     * Devuelve la clave de acceso pública de Unsplash.
     * Se usa al hacer las solicitudes (por ejemplo, dentro del ViewModel).
     */
    fun getAccessKey(): String = ACCESS_KEY
}
