package com.grupo12.guiaviajespersonalizada.ui.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.grupo12.guiaviajespersonalizada.R
import com.google.android.material.snackbar.Snackbar

class WebFragment : Fragment() {

    private var webView: WebView? = null
    private var etUrl: android.widget.EditText? = null
    private var btnGo: android.widget.Button? = null
    private var btnBack: android.widget.ImageButton? = null
    private var btnForward: android.widget.ImageButton? = null
    private var btnRefresh: android.widget.ImageButton? = null
    private var btnHome: android.widget.ImageButton? = null
    private var progressBar: android.widget.ProgressBar? = null
    private var tvPageTitle: android.widget.TextView? = null

    private val bookmarks = mutableListOf<WebBookmark>()
    private val history = mutableListOf<String>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_web, container, false)

        initializeViews(root)
        setupWebView()
        setupControls()
        loadDefaultSites()
        loadHomePage()

        return root
    }

    private fun initializeViews(root: View) {
        webView = root.findViewById(R.id.web_view)
        etUrl = root.findViewById(R.id.et_url)
        btnGo = root.findViewById(R.id.btn_go)
        btnBack = root.findViewById(R.id.btn_back)
        btnForward = root.findViewById(R.id.btn_forward)
        btnRefresh = root.findViewById(R.id.btn_refresh)
        btnHome = root.findViewById(R.id.btn_home)
        progressBar = root.findViewById(R.id.progress_bar)
        tvPageTitle = root.findViewById(R.id.tv_page_title)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView?.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar?.visibility = View.VISIBLE
                    etUrl?.setText(url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar?.visibility = View.GONE
                    url?.let {
                        if (it !in history) {
                            history.add(0, it)
                            if (history.size > 20) history.removeAt(20)
                        }
                        updateNavigationButtons()
                    }
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    progressBar?.visibility = View.GONE
                    showError("Error al cargar la página")
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar?.progress = newProgress
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    tvPageTitle?.text = title ?: "Página web"
                }
            }
        }
    }

    private fun setupControls() {
        // Campo de URL
        etUrl?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrl()
                true
            } else {
                false
            }
        }

        // Botón Go
        btnGo?.setOnClickListener {
            loadUrl()
        }

        // Botones de navegación
        btnBack?.setOnClickListener {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            }
        }

        btnForward?.setOnClickListener {
            if (webView?.canGoForward() == true) {
                webView?.goForward()
            }
        }

        btnRefresh?.setOnClickListener {
            webView?.reload()
        }

        btnHome?.setOnClickListener {
            loadUrl("https://www.google.com")
        }

        // Botones de sitios rápidos
        setupQuickSites()
    }

    private fun setupQuickSites() {
        view?.findViewById<android.widget.Button>(R.id.btn_booking)?.setOnClickListener {
            loadUrl("https://www.booking.com")
        }

        view?.findViewById<android.widget.Button>(R.id.btn_tripadvisor)?.setOnClickListener {
            loadUrl("https://www.tripadvisor.com")
        }

        view?.findViewById<android.widget.Button>(R.id.btn_airbnb)?.setOnClickListener {
            loadUrl("https://www.airbnb.com")
        }

        view?.findViewById<android.widget.Button>(R.id.btn_skyscanner)?.setOnClickListener {
            loadUrl("https://www.skyscanner.com")
        }

        view?.findViewById<android.widget.Button>(R.id.btn_lonely_planet)?.setOnClickListener {
            loadUrl("https://www.lonelyplanet.com")
        }
    }

    private fun loadDefaultSites() {
        bookmarks.addAll(listOf(
            WebBookmark("Google", "https://www.google.com"),
            WebBookmark("Booking.com", "https://www.booking.com"),
            WebBookmark("TripAdvisor", "https://www.tripadvisor.com"),
            WebBookmark("Airbnb", "https://www.airbnb.com"),
            WebBookmark("Skyscanner", "https://www.skyscanner.com"),
            WebBookmark("Lonely Planet", "https://www.lonelyplanet.com")
        ))
    }

    private fun loadHomePage() {
        webView?.loadUrl("https://www.google.com")
        etUrl?.setText("https://www.google.com")
    }

    private fun loadUrl() {
        var url = etUrl?.text.toString().trim()

        if (url.isEmpty()) {
            showError("Ingresa una URL válida")
            return
        }

        // Agregar protocolo si no lo tiene
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            // Si parece una búsqueda, usar Google
            if (!url.contains(".") || url.contains(" ")) {
                url = "https://www.google.com/search?q=${url.replace(" ", "+")}"
            } else {
                url = "https://$url"
            }
        }

        webView?.loadUrl(url)
    }

    private fun loadUrl(url: String) {
        webView?.loadUrl(url)
        etUrl?.setText(url)
    }

    private fun updateNavigationButtons() {
        val canGoBack = webView?.canGoBack() ?: false
        val canGoForward = webView?.canGoForward() ?: false

        btnBack?.isEnabled = canGoBack
        btnForward?.isEnabled = canGoForward

        btnBack?.alpha = if (canGoBack) 1.0f else 0.5f
        btnForward?.alpha = if (canGoForward) 1.0f else 0.5f
    }

    private fun showError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView?.destroy()
    }

    // Manejar el botón de retroceso del sistema
    fun onBackPressed(): Boolean {
        return if (webView?.canGoBack() == true) {
            webView?.goBack()
            true
        } else {
            false
        }
    }
}

// Data class para marcadores
data class WebBookmark(
    val title: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)