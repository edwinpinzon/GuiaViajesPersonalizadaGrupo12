package com.grupo12.guiaviajespersonalizada.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.grupo12.guiaviajespersonalizada.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar reproductor de video (convertir slideshow a video)
        setupVideoPlayer()

        // Observer para el ViewModel
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            // binding.textSlideshow.text = it
        }

        return root
    }

    private fun setupVideoPlayer() {
        // Configuración básica del reproductor de video
        // Por ahora solo estructura, luego se implementará la reproducción

        // Si tienes un VideoView en el layout:
        // binding.videoView.setVideoPath("android.resource://" + requireActivity().packageName + "/" + R.raw.sample_video)
        // binding.buttonPlay.setOnClickListener { binding.videoView.start() }
        // binding.buttonPause.setOnClickListener { binding.videoView.pause() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}