package com.grupo12.guiaviajespersonalizada

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvGuestMode: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
        setupUI()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)
        tvGuestMode = findViewById(R.id.tv_guest_mode)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            attemptLogin()
        }

        tvRegister.setOnClickListener {
            navigateToRegister()
        }

        tvGuestMode.setOnClickListener {
            navigateToMainAsGuest()
        }

        tvForgotPassword.setOnClickListener {
            handleForgotPassword()
        }
    }

    private fun setupUI() {
        // Ocultar action bar para pantalla de login limpia
        supportActionBar?.hide()

        // Configurar hints por defecto
        etEmail.hint = "ejemplo@email.com"
        etPassword.hint = "Tu contraseña"
    }

    private fun attemptLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Limpiar errores previos
        etEmail.error = null
        etPassword.error = null

        // Validación básica
        if (email.isEmpty()) {
            etEmail.error = "Email es requerido"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Contraseña es requerida"
            etPassword.requestFocus()
            return
        }

        if (!isValidEmail(email)) {
            etEmail.error = "Formato de email inválido"
            etEmail.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            etPassword.requestFocus()
            return
        }

        // Simular proceso de login
        performLogin(email, password)
    }

    private fun performLogin(email: String, password: String) {
        // Mostrar estado de carga
        btnLogin.isEnabled = false
        btnLogin.text = "Iniciando sesión..."

        // Simular delay de red (en una app real sería una llamada a API)
        btnLogin.postDelayed({
            // Simulación de login exitoso
            if (email == "demo@travelpal.com" && password == "123456") {
                loginSuccessful()
            } else {
                loginFailed()
            }
        }, 1500)
    }

    private fun loginSuccessful() {
        Toast.makeText(this, "¡Bienvenido a TravelPal! 🌍", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("user_email", etEmail.text.toString())
        intent.putExtra("login_successful", true)
        startActivity(intent)
        finish()
    }

    private fun loginFailed() {
        btnLogin.isEnabled = true
        btnLogin.text = "Iniciar Sesión"

        Snackbar.make(findViewById(android.R.id.content),
            "Credenciales incorrectas. Intenta: demo@travelpal.com / 123456",
            Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToRegister() {
        // En una app real, esto abriría RegisterActivity
        Toast.makeText(this, "Función de registro próximamente", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMainAsGuest() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("guest_mode", true)
        intent.putExtra("user_email", "Invitado")
        startActivity(intent)
        finish()
    }

    private fun handleForgotPassword() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Ingresa tu email primero", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Ingresa un email válido", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return
        }

        // Simular envío de recuperación
        Toast.makeText(this, "Instrucciones enviadas a $email 📧", Toast.LENGTH_LONG).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Sobrescribir el botón de retroceso para mostrar confirmación
    override fun onBackPressed() {
        Snackbar.make(findViewById(android.R.id.content),
            "¿Salir de TravelPal?",
            Snackbar.LENGTH_LONG)
            .setAction("Salir") {
                super.onBackPressed()
            }
            .show()
    }
}