package com.grupo12.guiaviajespersonalizada.ui.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.grupo12.guiaviajespersonalizada.R
import com.grupo12.guiaviajespersonalizada.databinding.FragmentGalleryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var photosAdapter: TravelPhotosAdapter
    private val photosList = mutableListOf<TravelPhoto>()

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) handleCameraResult(result.data)
    }

    private val pickPhotoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { handleGalleryResult(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root = binding.root

        setupRecyclerView()
        setupClickListeners()
        loadSamplePhotos()

        return root
    }

    private fun setupRecyclerView() {
        photosAdapter = TravelPhotosAdapter(photosList) { photo, action ->
            when (action) {
                PhotoAction.VIEW -> viewPhotoDetail(photo)
                PhotoAction.EDIT -> editPhoto(photo)
                PhotoAction.DELETE -> deletePhoto(photo)
                PhotoAction.SHARE -> sharePhoto(photo)
                PhotoAction.FAVORITE -> toggleFavorite(photo)
            }
        }

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvPhotos.apply {
            this.layoutManager = layoutManager
            adapter = photosAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.fabAddPhoto.setOnClickListener { showPhotoSourceDialog() }
        binding.btnChangeView.setOnClickListener { toggleViewMode() }
        binding.chipAllPhotos.setOnClickListener { filterPhotos(PhotoFilter.ALL) }
        binding.chipFavorites.setOnClickListener { filterPhotos(PhotoFilter.FAVORITES) }
        binding.chipRecent.setOnClickListener { filterPhotos(PhotoFilter.RECENT) }
    }

    private fun loadSamplePhotos() {
        val samplePhotos = listOf(
            TravelPhoto(
                id = 1,
                title = "Atardecer en París",
                description = "La Torre Eiffel iluminada al atardecer",
                location = "París, Francia",
                date = "2025-09-21",
                imageRes = R.drawable.ic_image_placeholder,
                isFavorite = true,
                tags = listOf("París", "Torre Eiffel", "Atardecer"),
                cameraInfo = "iPhone 13 Pro Max"
            ),
            TravelPhoto(
                id = 2,
                title = "Templo Senso-ji",
                description = "Templo tradicional en Asakusa",
                location = "Tokio, Japón",
                date = "2025-09-20",
                imageRes = R.drawable.ic_image_placeholder,
                isFavorite = false,
                tags = listOf("Tokio", "Templo", "Cultura"),
                cameraInfo = "Canon EOS R6"
            )
        )

        photosList.addAll(samplePhotos)
        photosAdapter.notifyDataSetChanged()
        updatePhotosCounter()
    }

    private fun showPhotoSourceDialog() {
        val options = arrayOf("📷 Tomar foto", "🖼️ Seleccionar de galería")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Agregar nueva foto de viaje")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null)
            takePhotoLauncher.launch(cameraIntent)
        else
            Toast.makeText(context, "Cámara no disponible", Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        pickPhotoLauncher.launch("image/*")
    }

    private fun handleCameraResult(data: Intent?) {
        val newPhoto = TravelPhoto(
            id = System.currentTimeMillis().toInt(),
            title = "Nueva foto ${photosList.size + 1}",
            description = "Capturada con la cámara",
            location = "Ubicación actual",
            date = "2025-09-22",
            imageRes = R.drawable.ic_image_placeholder,
            isFavorite = false,
            tags = listOf("Nueva", "Cámara"),
            cameraInfo = "Cámara del dispositivo"
        )
        addPhotoToList(newPhoto)
    }

    private fun handleGalleryResult(uri: android.net.Uri) {
        // Para mantener consistencia usamos drawable de ejemplo
        val newPhoto = TravelPhoto(
            id = System.currentTimeMillis().toInt(),
            title = "Foto importada ${photosList.size + 1}",
            description = "Seleccionada desde la galería",
            location = "Galería del dispositivo",
            date = "2025-09-22",
            imageRes = R.drawable.ic_image_placeholder,
            isFavorite = false,
            tags = listOf("Importada", "Galería"),
            cameraInfo = "Galería"
        )
        addPhotoToList(newPhoto)
    }

    private fun addPhotoToList(photo: TravelPhoto) {
        photosList.add(0, photo)
        photosAdapter.notifyItemInserted(0)
        binding.rvPhotos.scrollToPosition(0)
        Snackbar.make(binding.root, "📸 Foto agregada exitosamente", Snackbar.LENGTH_LONG)
            .setAction("Ver") { viewPhotoDetail(photo) }
            .show()
        updatePhotosCounter()
    }

    private fun viewPhotoDetail(photo: TravelPhoto) {
        val details = """
            📸 ${photo.title}
            
            📝 ${photo.description}
            
            📍 ${photo.location}
            📅 ${photo.date}
            📷 ${photo.cameraInfo}
            
            🏷️ Etiquetas: ${photo.tags.joinToString(", ")}
        """.trimIndent()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detalles de la foto")
            .setMessage(details)
            .setPositiveButton("Cerrar", null)
            .setNeutralButton("Compartir") { _, _ -> sharePhoto(photo) }
            .show()
    }

    private fun editPhoto(photo: TravelPhoto) {
        Toast.makeText(context, "Editor de fotos próximamente", Toast.LENGTH_SHORT).show()
    }

    private fun deletePhoto(photo: TravelPhoto) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar foto")
            .setMessage("¿Quieres eliminar '${photo.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                val position = photosList.indexOf(photo)
                if (position != -1) {
                    photosList.removeAt(position)
                    photosAdapter.notifyItemRemoved(position)
                    updatePhotosCounter()
                    Snackbar.make(binding.root, "Foto eliminada", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            photosList.add(position, photo)
                            photosAdapter.notifyItemInserted(position)
                            updatePhotosCounter()
                        }.show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun sharePhoto(photo: TravelPhoto) {
        val shareText = """
            📸 ${photo.title}
            
            📍 ${photo.location}
            📅 ${photo.date}
            
            ${photo.description}
            
            #TravelPal #Viajes #${photo.tags.joinToString(" #")}
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Mira esta foto de mi viaje!")
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir foto"))
    }

    private fun toggleFavorite(photo: TravelPhoto) {
        photo.isFavorite = !photo.isFavorite
        val position = photosList.indexOf(photo)
        if (position != -1) photosAdapter.notifyItemChanged(position)
        val message = if (photo.isFavorite) "💖 Agregada a favoritos" else "Removida de favoritos"
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun toggleViewMode() {
        val layoutManager = binding.rvPhotos.layoutManager
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                binding.rvPhotos.layoutManager = GridLayoutManager(context, 1)
                binding.btnChangeView.text = "📱 Grid"
            }
            is GridLayoutManager -> {
                layoutManager.spanCount = if (layoutManager.spanCount == 1) 2 else 1
                binding.btnChangeView.text = if (layoutManager.spanCount == 1) "📋 Lista" else "🎯 Masonry"
            }
        }
    }

    private fun filterPhotos(filter: PhotoFilter) {
        val filtered = when (filter) {
            PhotoFilter.ALL -> photosList
            PhotoFilter.FAVORITES -> photosList.filter { it.isFavorite }
            PhotoFilter.RECENT -> photosList.takeLast(10)
        }
        binding.chipAllPhotos.isChecked = filter == PhotoFilter.ALL
        binding.chipFavorites.isChecked = filter == PhotoFilter.FAVORITES
        binding.chipRecent.isChecked = filter == PhotoFilter.RECENT
        updatePhotosList(filtered)
    }

    private fun updatePhotosList(photos: List<TravelPhoto>) {
        photosList.clear()
        photosList.addAll(photos)
        photosAdapter.notifyDataSetChanged()
        updatePhotosCounter()
    }

    private fun updatePhotosCounter() {
        val total = photosList.size
        val favs = photosList.count { it.isFavorite }
        binding.tvPhotosCounter.text = "📸 $total fotos • 💖 $favs favoritos"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Data class con tipos de raíz ajustados
data class TravelPhoto(
    val id: Int,
    var title: String,
    var description: String,
    val location: String,
    val date: String,       // ahora String legible
    val imageRes: Int,      // drawable
    var isFavorite: Boolean,
    val tags: List<String> = emptyList(),
    val cameraInfo: String = "Desconocida"
)

enum class PhotoAction { VIEW, EDIT, DELETE, SHARE, FAVORITE }
enum class PhotoFilter { ALL, FAVORITES, RECENT }
