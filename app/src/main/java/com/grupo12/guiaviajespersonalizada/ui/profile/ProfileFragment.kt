package com.grupo12.guiaviajespersonalizada.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.grupo12.guiaviajespersonalizada.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        // Configurar información básica del perfil
        val tvUserName: TextView = root.findViewById(R.id.tv_user_name)
        val tvUserEmail: TextView = root.findViewById(R.id.tv_user_email)
        val tvEducation: TextView = root.findViewById(R.id.tv_education)
        val tvExperience: TextView = root.findViewById(R.id.tv_experience)

        // Datos de ejemplo
        tvUserName.text = "Juan Carlos Pérez"
        tvUserEmail.text = "jcarlos.perez@email.com"

        tvEducation.text = """
            🎓 Ingeniería de Sistemas
            Universidad Nacional de Colombia (2018-2022)
            
            📚 Certificaciones:
            • AWS Solutions Architect
            • Google Cloud Professional Developer
            • Scrum Master Certified
        """.trimIndent()

        tvExperience.text = """
            💼 Desarrollador Full Stack Senior
            TechCorp Solutions (2022 - Presente)
            
            👨‍💻 Desarrollador Junior  
            StartupTech (2021 - 2022)
            
            🎯 Freelancer
            Diversos proyectos (2020 - 2021)
        """.trimIndent()

        return root
    }
}