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

/**
 * MainActivity: Gestisce l'interfaccia utente (UI) dell'app
 * Un'app può contenere diverse Activity, che sono ampiamente indipendenti
 */
class MainActivity : AppCompatActivity()
{
    // Class variables
    private lateinit var tv : TextView
    private lateinit var bu : Button
    private lateinit var bu2 : Button

    // Chiamato quando l'activity viene creata per la prima volta
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35.
        WindowCompat.enableEdgeToEdge(window)

        // Visualizza il layout
        setContentView(R.layout.activity_main)

        // Ottiene i riferimenti agli oggetti UI dopo setContentView()
        tv = findViewById(R.id.tv)
        bu = findViewById(R.id.bu)
        bu2 = findViewById(R.id.bu2)

        /* * TIP: RIPRISTINO DELLO STATO DELL'ISTANZA (INSTANCE STATE)
         * Se l'activity è stata uccisa e ricreata dal sistema (es. rotazione schermo),
         * il nuovo oggetto MainActivity eredita lo stato dal bundle savedInstanceState
         */
        if (savedInstanceState != null) {
            // Recupera la stringa precedentemente salvata con la chiave "strTV"
            val strValue = savedInstanceState.getString("strTV")
            if (strValue != null) tv.text = strValue
        }

        // Set the action to be performed when the first button is pressed
        bu.setOnClickListener { // Perform action on click
            tv.text = getString(R.string.good_job)
        }

        // Set the action to be performed when the second button is pressed
        bu2.setOnClickListener {
            val i: Int = try {
                tv.text.toString().toInt()
            } catch(exception: NumberFormatException) {
                -1
            }
            tv.text = getString(R.string.int_number, i+1)
        }

        // Ensure that system bars remain visible regardless of the background color.
        manageSystemBarsAppearance(findViewById(R.id.cl))
    }

    /**
     * Chiamato dal sistema prima di distruggere l'activity per recuperare risorse
     * o a causa di un cambio di configurazione (es. rotazione)
     * * Permette di salvare lo "Instance State": informazioni associate a una specifica
     * istanza dell'activity che devono sopravvivere alla ricreazione
     */
    override fun onSaveInstanceState(outState: Bundle)
    {
        // Note: con l'implementazione di default di AppCompatActivity, alcuni widget
        // salvano lo stato automaticamente. È necessario personalizzarlo per elementi non autosalvanti.
        super.onSaveInstanceState(outState)

        /* * Salvataggio dello stato della TextView nel Bundle
         * Questo stato verrà scartato se l'utente chiude l'app normalmente (es. tasto Back).
         * Nota: Lo stato PERSISTENTE (es. preferenze utente) andrebbe salvato in onPause()
         */
        outState.putString("strTV", tv.text.toString())
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
