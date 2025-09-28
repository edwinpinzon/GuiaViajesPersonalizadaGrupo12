package com.grupo12.guiaviajespersonalizada

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.grupo12.guiaviajespersonalizada.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Variables para datos del usuario
    private var userEmail: String = "usuario@travelpal.com"
    private var isGuestMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("NAV_DEBUG", "MainActivity creada")

        // Configurar toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Obtener datos del Intent (del Login)
        getUserDataFromIntent()

        // Configurar FAB
        setupFloatingActionButton()

        // Configurar Navigation Drawer
        setupNavigationDrawer()

        // Actualizar header del menú lateral
        updateNavigationHeader()
    }

    private fun getUserDataFromIntent() {
        userEmail = intent.getStringExtra("user_email") ?: "usuario@travelpal.com"
        isGuestMode = intent.getBooleanExtra("guest_mode", false)
        val loginSuccessful = intent.getBooleanExtra("login_successful", false)

        Log.d("NAV_DEBUG", "User email: $userEmail | Guest: $isGuestMode")

        if (loginSuccessful) {
            showWelcomeMessage()
        }
    }

    private fun setupFloatingActionButton() {
        binding.appBarMain.fab.setOnClickListener { view ->
            Log.d("NAV_DEBUG", "Click en FAB")
            val message = if (isGuestMode) {
                "Regístrate para buscar destinos personalizados"
            } else {
                "Buscar nuevo destino"
            }

            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Buscar") {
                    Log.d("NAV_DEBUG", "Acción Buscar ejecutada")
                    navigateToSearch()
                }.show()
        }
    }

    private fun setupNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        Log.d("NAV_DEBUG", "Config Drawer + NavController listo")

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_gallery,
                R.id.nav_video,
                R.id.nav_web,
                R.id.nav_buttons,    // ✅ agregado aquí
                R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            Log.d("NAV_DEBUG", "Click en menú: ${menuItem.title}")
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("NAV_DEBUG", "Navegando -> HomeFragment")
                    navController.navigate(R.id.nav_home)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_profile -> {
                    Log.d("NAV_DEBUG", "Navegando -> ProfileFragment (guest=$isGuestMode)")
                    if (isGuestMode) {
                        showGuestModeMessage("perfil")
                        false
                    } else {
                        navController.navigate(R.id.nav_profile)
                        drawerLayout.closeDrawers()
                        true
                    }
                }
                R.id.nav_gallery -> {
                    Log.d("NAV_DEBUG", "Navegando -> GalleryFragment")
                    navController.navigate(R.id.nav_gallery)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_video -> {
                    Log.d("NAV_DEBUG", "Navegando -> VideoFragment")
                    navController.navigate(R.id.nav_video)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_web -> {
                    Log.d("NAV_DEBUG", "Navegando -> WebFragment")
                    navController.navigate(R.id.nav_web)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_buttons -> {
                    Log.d("NAV_DEBUG", "Navegando -> NavigationControlFragment")
                    navController.navigate(R.id.nav_buttons)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_slideshow -> {
                    Log.d("NAV_DEBUG", "Navegando -> SlideshowFragment")
                    navController.navigate(R.id.nav_slideshow)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    Log.e("NAV_DEBUG", "Click en item desconocido: ${menuItem.itemId}")
                    false
                }
            }
        }
    }

    private fun updateNavigationHeader() {
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)

        val tvUserName = headerView.findViewById<android.widget.TextView>(R.id.textView)
        val tvUserEmail = headerView.findViewById<android.widget.TextView>(R.id.textView)

        if (isGuestMode) {
            tvUserName?.text = "Invitado"
            tvUserEmail?.text = "Modo exploración"
        } else {
            tvUserName?.text = getUserNameFromEmail(userEmail)
            tvUserEmail?.text = userEmail
        }

        Log.d("NAV_DEBUG", "Header actualizado -> User: ${tvUserName?.text}, Email: ${tvUserEmail?.text}")
    }

    private fun getUserNameFromEmail(email: String): String {
        return email.substringBefore("@").replace(".", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }

    private fun showWelcomeMessage() {
        val userName = getUserNameFromEmail(userEmail)
        Log.d("NAV_DEBUG", "Mostrando mensaje de bienvenida a $userName")
        Snackbar.make(
            binding.root,
            "Bienvenido $userName! Listo para tu próxima aventura",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showGuestModeMessage(feature: String) {
        Log.d("NAV_DEBUG", "Modo invitado intentando acceder a $feature")
        Snackbar.make(
            binding.root,
            "Inicia sesión para acceder a tu $feature",
            Snackbar.LENGTH_LONG
        ).setAction("Login") {
            finish()
        }.show()
    }

    private fun navigateToSearch() {
        Log.d("NAV_DEBUG", "Navegando -> Home (Search)")
        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (isGuestMode) {
            menu.findItem(R.id.action_settings)?.isVisible = false
        }
        Log.d("NAV_DEBUG", "Menú de opciones creado (guest=$isGuestMode)")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                if (isGuestMode) {
                    showGuestModeMessage("configuración")
                } else {
                    Snackbar.make(binding.root, "Configuración próximamente", Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
            drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
        } else {
            Snackbar.make(
                binding.root,
                "Salir de TravelPal?",
                Snackbar.LENGTH_LONG
            ).setAction("Salir") {
                super.onBackPressed()
            }.show()
        }
    }
}
