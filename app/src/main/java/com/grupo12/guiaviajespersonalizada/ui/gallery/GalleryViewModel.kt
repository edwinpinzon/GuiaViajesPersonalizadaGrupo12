package com.grupo12.guiaviajespersonalizada.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grupo12.guiaviajespersonalizada.R

class GalleryViewModel : ViewModel() {

    private val _photos = MutableLiveData<List<TravelPhoto>>()
    val photos: LiveData<List<TravelPhoto>> = _photos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedFilter = MutableLiveData<PhotoFilter>()
    val selectedFilter: LiveData<PhotoFilter> = _selectedFilter

    init {
        _selectedFilter.value = PhotoFilter.ALL
        loadPhotos()
    }

    fun loadPhotos() {
        _isLoading.value = true
        _photos.value = getSamplePhotos()
        _isLoading.value = false
    }

    fun filterPhotos(filter: PhotoFilter) {
        _selectedFilter.value = filter
        val allPhotos = getSamplePhotos()
        val filteredPhotos = when (filter) {
            PhotoFilter.ALL -> allPhotos
            PhotoFilter.FAVORITES -> allPhotos.filter { it.isFavorite }
            PhotoFilter.RECENT -> allPhotos.takeLast(10)
        }
        _photos.value = filteredPhotos
    }

    fun addPhoto(photo: TravelPhoto) {
        val currentPhotos = _photos.value?.toMutableList() ?: mutableListOf()
        currentPhotos.add(0, photo)
        _photos.value = currentPhotos
    }

    fun removePhoto(photoId: Int) {   // ✅ Cambiado a Int
        val currentPhotos = _photos.value?.toMutableList() ?: mutableListOf()
        currentPhotos.removeAll { it.id == photoId }
        _photos.value = currentPhotos
    }

    fun toggleFavorite(photoId: Int) {  // ✅ Cambiado a Int
        val currentPhotos = _photos.value?.toMutableList() ?: mutableListOf()
        val photo = currentPhotos.find { it.id == photoId }
        photo?.let {
            it.isFavorite = !it.isFavorite
            _photos.value = currentPhotos
        }
    }

    private fun getSamplePhotos(): List<TravelPhoto> {
        return listOf(
            TravelPhoto(
                id = 1,
                title = "Torre Eiffel",
                description = "Vista de la Torre Eiffel en París",
                location = "París, Francia",
                date = "2025-09-21",
                imageRes = R.drawable.ic_image_placeholder,
                isFavorite = false,
                tags = listOf("París", "Torre Eiffel"),
                cameraInfo = "iPhone 13 Pro Max"
            ),
        )
    }
}
