package com.grupo12.guiaviajespersonalizada.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.grupo12.guiaviajespersonalizada.databinding.FragmentNavigationControlBinding

class NavigationControlFragment : Fragment() {

    private var _binding: FragmentNavigationControlBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigationInstructions()
        setupFeatureExplanations()
    }

    private fun setupNavigationInstructions() {
        // Botón de instrucciones del menú hamburguesa
        binding.btnMenuNavigation.setOnClickListener {
            showInstructionDialog(
                "Navegación del Menú",
                "Usa el menú hamburguesa (☰) en la esquina superior izquierda para navegar entre secciones:\n\n" +
                        "• Home: Pantalla principal con destinos\n" +
                        "• Perfil: Gestiona tu información personal\n" +
                        "• Fotos: Galería personal de viajes\n" +
                        "• Video: Reproductor multimedia\n" +
                        "• Web: Navegador integrado\n" +
                        "• Navigation & Control: Esta sección de ayuda"
            )
        }

        // Botón de búsqueda
        binding.btnSearchInstructions.setOnClickListener {
            showInstructionDialog(
                "Función de Búsqueda",
                "Usa el icono de búsqueda (🔍) para encontrar destinos:\n\n" +
                        "• Escribe el nombre del destino\n" +
                        "• Selecciona fechas de viaje\n" +
                        "• Filtra por categorías\n" +
                        "• Presiona 'Buscar' para obtener resultados"
            )
        }

        // Botón de favoritos
        binding.btnFavoriteInstructions.setOnClickListener {
            showInstructionDialog(
                "Botón de Favoritos",
                "Administra tus lugares favoritos:\n\n" +
                        "• Toca el corazón (♥) en cualquier destino\n" +
                        "• Los favoritos se guardan en tu perfil\n" +
                        "• Accede rápidamente desde tu perfil\n" +
                        "• Sincroniza entre dispositivos"
            )
        }

        // Botón de compartir
        binding.btnShareInstructions.setOnClickListener {
            showInstructionDialog(
                "Función Compartir",
                "Comparte tus experiencias de viaje:\n\n" +
                        "• Usa el ícono compartir (📤)\n" +
                        "• Comparte destinos por redes sociales\n" +
                        "• Envía recomendaciones a amigos\n" +
                        "• Exporta tu itinerario de viaje"
            )
        }

        // Botón de filtros
        binding.btnFilterInstructions.setOnClickListener {
            showInstructionDialog(
                "Filtros de Búsqueda",
                "Personaliza tu búsqueda de destinos:\n\n" +
                        "• Filtrar por precio\n" +
                        "• Seleccionar tipo de actividad\n" +
                        "• Filtrar por región\n" +
                        "• Ordenar por popularidad o precio"
            )
        }
    }

    private fun setupFeatureExplanations() {
        // Explicación del perfil
        binding.btnProfileExplanation.setOnClickListener {
            showInstructionDialog(
                "Personalización del Perfil",
                "Gestiona tu perfil de viajero:\n\n" +
                        "• Sube tu foto de perfil\n" +
                        "• Completa tu información personal\n" +
                        "• Añade tus experiencias de viaje\n" +
                        "• Actualiza tu ubicación\n" +
                        "• Administra tus preferencias"
            )
        }

        // Explicación de fotos
        binding.btnPhotosExplanation.setOnClickListener {
            showInstructionDialog(
                "Galería de Fotos",
                "Tu álbum personal de viajes:\n\n" +
                        "• Sube fotos desde tu cámara o galería\n" +
                        "• Organiza por destinos visitados\n" +
                        "• Añade descripciones a tus fotos\n" +
                        "• Comparte tus mejores momentos\n" +
                        "• Navega con scroll dinámico"
            )
        }

        // Explicación de videos
        binding.btnVideoExplanation.setOnClickListener {
            showInstructionDialog(
                "Reproductor de Video",
                "Biblioteca multimedia personal:\n\n" +
                        "• Sube videos de tus viajes\n" +
                        "• Controles de reproducción estándar\n" +
                        "• Play, pause, avanzar, retroceder\n" +
                        "• Almacena tus mejores momentos\n" +
                        "• Crea tu diario audiovisual"
            )
        }

        // Explicación del navegador web
        binding.btnWebExplanation.setOnClickListener {
            showInstructionDialog(
                "Navegador Web Integrado",
                "Acceso a servicios externos:\n\n" +
                        "• Ingresa URLs manualmente\n" +
                        "• Accede a Booking, TripAdvisor, Airbnb\n" +
                        "• Navega sin salir de la app\n" +
                        "• Botones de navegación integrados\n" +
                        "• Solo sitios seguros (HTTPS)"
            )
        }

        // Botón de demostración interactiva
        binding.btnInteractiveDemo.setOnClickListener {
            startInteractiveDemo()
        }

        // Botón de ayuda adicional
        binding.btnAdditionalHelp.setOnClickListener {
            showInstructionDialog(
                "Ayuda Adicional",
                "¿Necesitas más ayuda?\n\n" +
                        "• Revisa estas instrucciones cuando lo necesites\n" +
                        "• Cada sección tiene sus propias funcionalidades\n" +
                        "• Usa el menú para navegar fácilmente\n" +
                        "• ¡Explora y disfruta tu experiencia de viaje!"
            )
        }
    }

    private fun showInstructionDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun startInteractiveDemo() {
        AlertDialog.Builder(requireContext())
            .setTitle("Demo Interactivo")
            .setMessage("Prueba navegando por el menú hamburguesa y explorando cada sección!")
            .setPositiveButton("Entendido") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
