package it.unipd.dei.esp2021.hellowithbuttond

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35.
        // Must be done programmatically because it depends on the Android version at runtime
        WindowCompat.enableEdgeToEdge(window)

        // Display the layout
        setContentView(R.layout.activity_main) // riceve come parametro l'indice di una risorsa
        // occupo lo spazio con la gerarchia di risorso contenute in activity_main
        // public static abstract int activity_main = 1300000

        // Get references to UI objects
        // Do it AFTER setContentView()! Before setContentView()
        // the objects have not been instantiated yet
        // Once and for all: Kotlin synthetics are not a recommended practice
        // (https://proandroiddev.com/the-argument-over-kotlin-synthetics-735305dd4ed0)
        val tv : TextView = findViewById(R.id.tv) // public static abstract int tv = 1000000
        val bu : Button = findViewById(R.id.bu) // il nome deve essere uguale a quello contenuto nel file XML
        // in R ho la sezione con le reference di tutte le risorse, devo solo recuperata l'indice della risorsa


        // Set the action to be performed when the button is pressed
        bu.setOnClickListener { // Perform action on click
            tv.text = getString(R.string.good_job)
        }

        // Ensure that system bars remain visible regardless of the background color.
        // Must be done programmatically because it depends on the device theme at runtime
        manageSystemBarsAppearance(findViewById(R.id.cl))
    }

    fun manageSystemBarsAppearance(rootView: View) {
        val nightModeMask = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeMask == Configuration.UI_MODE_NIGHT_NO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val appearanceMask =
                    APPEARANCE_LIGHT_NAVIGATION_BARS or
                            APPEARANCE_LIGHT_STATUS_BARS
                window.insetsController?.setSystemBarsAppearance(appearanceMask, appearanceMask)
            } else {
                val newVis = rootView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                rootView.systemUiVisibility = newVis
            }
        }
    }
}
