package com.grupo12.guiaviajespersonalizada.ui.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.grupo12.guiaviajespersonalizada.databinding.FragmentVideoBinding
import android.util.Log

class VideoFragment : Fragment() {

    private val TAG = "VideoFragment"
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private var mediaController: MediaController? = null
    private var currentVideoPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: inflando layout del fragmento")
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        try {
            setupVideoPlayer()
            loadSampleVideos()
            setupControls()
        } catch (e: Exception) {
            Log.e(TAG, "Error en onCreateView", e)
        }

        return root
    }

    private fun setupVideoPlayer() {
        Log.d(TAG, "setupVideoPlayer: iniciando configuración del reproductor")

        try {
            mediaController = MediaController(requireContext())
            Log.d(TAG, "MediaController creado")

            mediaController?.setAnchorView(binding.videoView)
            binding.videoView.setMediaController(mediaController)

            val videoUri =
                Uri.parse("https://www.html5rocks.com/en/tutorials/video/basics/devstories.webm")
            Log.d(TAG, "Video URI asignado: $videoUri")

            binding.videoView.setVideoURI(videoUri)

            binding.videoView.setOnPreparedListener { mediaPlayer ->
                Log.d(TAG, "Video preparado correctamente")
                mediaPlayer.isLooping = true
                binding.videoView.seekTo(currentVideoPosition)
                if (currentVideoPosition == 0) {
                    binding.videoView.start()
                    Log.d(TAG, "Video iniciado automáticamente")
                }
                binding.progressBar.visibility = View.GONE
            }

            binding.videoView.setOnCompletionListener {
                Log.d(TAG, "Video finalizado")
                binding.btnPlay.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en setupVideoPlayer", e)
        }
    }

    private fun loadSampleVideos() {
        Log.d(TAG, "loadSampleVideos: cargando lista de videos")

        try {
            val videos = listOf(
                VideoItem("Destinos Increíbles", "Descubre los destinos más hermosos", "https://example.com/video1.mp4"),
                VideoItem("Aventuras en Tokio", "Explora la capital japonesa", "https://example.com/video2.mp4"),
                VideoItem("París Romántico", "La ciudad del amor", "https://example.com/video3.mp4")
            )

            binding.tvVideoTitle.text = videos.first().title
            binding.tvVideoDescription.text = videos.first().description
            Log.d(TAG, "Primer video cargado: ${videos.first().title}")
        } catch (e: Exception) {
            Log.e(TAG, "Error en loadSampleVideos", e)
        }
    }

    private fun setupControls() {
        Log.d(TAG, "setupControls: configurando botones de control")

        try {
            binding.btnPlay.setOnClickListener {
                if (binding.videoView.isPlaying) {
                    Log.d(TAG, "Botón Play: pausando video")
                    binding.videoView.pause()
                    binding.btnPlay.text = "▶️ Reproducir"
                } else {
                    Log.d(TAG, "Botón Play: reproduciendo video")
                    binding.videoView.start()
                    binding.btnPlay.text = "⏸️ Pausar"
                }
            }

            binding.btnFullscreen.setOnClickListener {
                Log.d(TAG, "Botón Fullscreen presionado")
                toggleFullscreen()
            }

            binding.btnShare.setOnClickListener {
                Log.d(TAG, "Botón Share presionado")
                shareVideo()
            }

            binding.btnVolumeUp.setOnClickListener {
                Log.d(TAG, "Botón Volume Up presionado")
            }

            binding.btnVolumeDown.setOnClickListener {
                Log.d(TAG, "Botón Volume Down presionado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en setupControls", e)
        }
    }

    private fun toggleFullscreen() {
        Log.d(TAG, "toggleFullscreen: cambiando tamaño del reproductor")

        try {
            val message =
                if (binding.videoView.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    binding.videoView.layoutParams.height = 600
                    "Modo normal"
                } else {
                    binding.videoView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    "Pantalla completa"
                }

            binding.videoView.requestLayout()
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
            Log.d(TAG, "toggleFullscreen: $message")
        } catch (e: Exception) {
            Log.e(TAG, "Error en toggleFullscreen", e)
        }
    }

    private fun shareVideo() {
        Log.d(TAG, "shareVideo: compartiendo video")

        try {
            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    android.content.Intent.EXTRA_TEXT,
                    "¡Mira este increíble video de viaje! 🎬\n${binding.tvVideoTitle.text}"
                )
            }
            startActivity(android.content.Intent.createChooser(shareIntent, "Compartir video"))
        } catch (e: Exception) {
            Log.e(TAG, "Error en shareVideo", e)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: guardando posición actual del video")
        try {
            if (binding.videoView.isPlaying) {
                currentVideoPosition = binding.videoView.currentPosition
                binding.videoView.pause()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en onPause", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: restaurando posición del video: $currentVideoPosition")
        try {
            binding.videoView.seekTo(currentVideoPosition)
        } catch (e: Exception) {
            Log.e(TAG, "Error en onResume", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: liberando recursos del video")
        try {
            binding.videoView.stopPlayback()
            _binding = null
        } catch (e: Exception) {
            Log.e(TAG, "Error en onDestroyView", e)
        }
    }
}

// Data class para videos
data class VideoItem(
    val title: String,
    val description: String,
    val url: String
)
