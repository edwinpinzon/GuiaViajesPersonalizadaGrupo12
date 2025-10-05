package com.grupo12.guiaviajespersonalizada.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.grupo12.guiaviajespersonalizada.R
import com.grupo12.guiaviajespersonalizada.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var destinationsAdapter: PopularDestinationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSearchFunction()
        setupPopularDestinations()
        setupObservers()
        loadInitialData()

        return root
    }

    // 🔹 Configuración del buscador y chips
    private fun setupSearchFunction() {
        binding.btnSearchDestinations.setOnClickListener { performSearch() }

        binding.chipParis.setOnClickListener { quickSearch("París") }
        binding.chipTokyo.setOnClickListener { quickSearch("Tokio") }
        binding.chipNewYork.setOnClickListener { quickSearch("Nueva York") }
        binding.chipLondon.setOnClickListener { quickSearch("Londres") }
    }

    // 🔹 Configura la lista horizontal de destinos
    private fun setupPopularDestinations() {
        destinationsAdapter = PopularDestinationsAdapter { destination ->
            onDestinationClicked(destination)
        }

        binding.rvPopularDestinations.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = destinationsAdapter
        }

        binding.btnViewAllDestinations.setOnClickListener {
            showAllDestinations()
        }
    }

    // 🔹 Observadores del ViewModel (actualizados)
    private fun setupObservers() {
        homeViewModel.popularDestinations.observe(viewLifecycleOwner) { destinations ->
            destinationsAdapter.updateDestinations(destinations)

            // Si se agregó un resultado de búsqueda, hacer scroll y mostrar aviso
            val hasSearchResult = destinations.any { it.id == 999 }
            if (hasSearchResult) {
                binding.rvPopularDestinations.scrollToPosition(0)
                Snackbar.make(
                    binding.root,
                    "🌍 Resultado agregado arriba en destinos",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            showSearchResults(results)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSearchDestinations.isEnabled = !isLoading
        }
    }

    // 🔹 Carga inicial
    private fun loadInitialData() {
        homeViewModel.loadPopularDestinations()
        showWelcomeMessage()
    }

    // 🔹 Búsqueda con validaciones
    private fun performSearch() {
        val destination = binding.etWhereTo.text.toString().trim()
        val travelDate = binding.etWhenTravel.text.toString().trim()
        val travelers = binding.etTravelers.text.toString().trim()

        if (destination.isEmpty()) {
            binding.etWhereTo.error = "¿A dónde quieres ir?"
            binding.etWhereTo.requestFocus()
            return
        }

        if (travelDate.isEmpty()) {
            binding.etWhenTravel.error = "¿Cuándo planeas viajar?"
            binding.etWhenTravel.requestFocus()
            return
        }

        val travelersCount = travelers.toIntOrNull() ?: 1
        if (travelersCount < 1) {
            binding.etTravelers.error = "Al menos 1 viajero"
            binding.etTravelers.requestFocus()
            return
        }

        // 🔹 Ejecutar búsqueda (usa la versión mejorada del ViewModel)
        homeViewModel.searchDestinations(destination, travelDate, travelersCount)

        // Feedback visual
        showSearchFeedback(destination, travelDate, travelersCount)

        // Cargar imagen de fondo del destino buscado
        loadBackgroundImage(destination)
    }

    private fun quickSearch(destination: String) {
        binding.etWhereTo.setText(destination)
        binding.etWhenTravel.setText("Próximamente")
        binding.etTravelers.setText("2")
        performSearch()
    }

    private fun showSearchFeedback(destination: String, date: String, travelers: Int) {
        val message =
            "🔍 Buscando viajes a $destination para $travelers ${if (travelers == 1) "persona" else "personas"}"

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Ver resultados") {
                showDetailedResults(destination, date, travelers)
            }
            .show()
    }

    // 🔹 Mostrar resultados
    private fun showSearchResults(results: List<SearchResult>) {
        if (results.isEmpty()) {
            Toast.makeText(
                context,
                "No se encontraron resultados. Intenta con otro destino.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val message = "¡Encontramos ${results.size} opciones increíbles!"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDetailedResults(destination: String, date: String, travelers: Int) {
        val results = """
            🎯 Resultados para tu búsqueda:
            
            📍 Destino: $destination
            📅 Fecha: $date  
            👥 Viajeros: $travelers
            
            💡 Sugerencias encontradas:
            • Hoteles desde $299/noche
            • Vuelos desde $599 por persona
            • Actividades desde $49/día
            
            ¡Explora las opciones en los otros fragmentos!
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Resultados de Búsqueda")
            .setMessage(results)
            .setPositiveButton("¡Perfecto!") { _, _ -> }
            .setNeutralButton("Buscar más") { _, _ -> clearSearchFields() }
            .show()
    }

    private fun onDestinationClicked(destination: PopularDestination) {
        binding.etWhereTo.setText(destination.name)
        binding.etWhenTravel.setText("Próximamente")
        binding.etTravelers.setText("2")

        val message = "✈️ ${destination.name} seleccionado. Precio desde ${destination.price}"
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Buscar ahora") { performSearch() }
            .show()

        loadBackgroundImage(destination.name)
    }

    private fun loadBackgroundImage(query: String) {
        val photoUrl = "https://source.unsplash.com/featured/?$query"

        Glide.with(requireContext())
            .load(photoUrl)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(binding.ivBackgroundImage)
    }

    private fun showAllDestinations() {
        Toast.makeText(context, "Ver todos los destinos - Próximamente", Toast.LENGTH_SHORT).show()
    }

    private fun clearSearchFields() {
        binding.etWhereTo.setText("")
        binding.etWhenTravel.setText("")
        binding.etTravelers.setText("")
        binding.etWhereTo.requestFocus()
    }

    private fun showWelcomeMessage() {
        val welcomeMessages = listOf(
            "¡Bienvenido a TravelPal! 🌍 ¿Listo para tu próxima aventura?",
            "🛫 ¿A dónde te llevamos hoy?",
            "✨ Descubre destinos increíbles con TravelPal",
            "🎒 Tu próximo viaje comienza aquí"
        )

        val randomMessage = welcomeMessages.random()
        view?.postDelayed({
            Snackbar.make(binding.root, randomMessage, Snackbar.LENGTH_LONG)
                .setAction("Explorar") {
                    binding.scrollView.smoothScrollTo(0, binding.rvPopularDestinations.top)
                }
                .show()
        }, 1000)
    }

    private fun hideFloatingActionButton() {
        try {
            val fab = activity?.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
                R.id.fab
            )
            fab?.visibility = View.GONE
        } catch (_: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        hideFloatingActionButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            val fab = activity?.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
                R.id.fab
            )
            fab?.visibility = View.VISIBLE
        } catch (_: Exception) {
        }
        _binding = null
    }
}
