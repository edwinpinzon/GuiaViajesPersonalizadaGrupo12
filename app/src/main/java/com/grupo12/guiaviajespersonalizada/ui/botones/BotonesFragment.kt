
package com.grupo12.guiaviajespersonalizada.ui.botones
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.grupo12.guiaviajespersonalizada.databinding.FragmentBotonesBinding

class BotonesFragment : Fragment() {

    private var _binding: FragmentBotonesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBotonesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupFloatingActionButton()
    }

    private fun setupButtons() {
        // Configuración de botones del grid
        binding.button1.setOnClickListener {
            showToast("Botón 1 presionado")
        }

        binding.button2.setOnClickListener {
            showToast("Botón 2 presionado")
        }

        binding.button3.setOnClickListener {
            showToast("Botón 3 presionado")
        }

        binding.button4.setOnClickListener {
            showToast("Botón 4 presionado")
        }

        binding.button5.setOnClickListener {
            showToast("Botón 5 presionado")
        }

        binding.button6.setOnClickListener {
            showToast("Botón 6 presionado")
        }
    }

    private fun setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener {
            showToast("FloatingActionButton presionado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}