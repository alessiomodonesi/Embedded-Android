package it.unipd.dei.esp2021.hellowithbutton

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity()
{
    // Called when the activity is first created
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Create the TextView
        val tv = TextView(this) // gli oggetti derivati da view devono sapere in che oggetti esistono
        tv.text = "Press the button, please" // chiamo il setter

        // Create the Button
        val bu1 = Button(this)
        bu1.text = "Press me" // oggetti simili -> funzioni simili, es. ".text"

        // Create the second Button
        val bu2 = Button(this)
        bu2.text = "+1"

        // Set the action to be performed when the button is pressed
        bu1.setOnClickListener {
            tv.text = "Good Job!"
        }

        bu2.setOnClickListener {
            val i : Int = try { // lambda function
                tv.text.toString().toInt() // provo a convertire in intero
            } catch (e: NumberFormatException) { // in caso di eccezione setto a -1
                -1
            }
            tv.text = "${i + 1}"
        }

        // All UI elements must have IDs to use ConstraintSet
        bu1.id = View.generateViewId() // ConstraintLayout ha bisogno di IDs per gli oggetti
        bu2.id = View.generateViewId()
        tv.id = View.generateViewId()

        // Create the layout (contenitore degli oggetti)
        val myLayout = ConstraintLayout(this)

        // Add the UI elements to the layout
        myLayout.addView(bu1) // Button e Textview sono derivate da View
        myLayout.addView(bu2)
        myLayout.addView(tv)

        // Add constraints to the layout so that UI elements are positioned correctly
        val mySet = ConstraintSet()
        mySet.clone(myLayout) // inizializzo il set con le informazioni estratte dal layout

        // imposto dei vincoli tra gli oggetti -> come collegare due oggetti tramite una molla
        // più vincoli in combo determinano dove un oggetto deve stare

        // collego il lato sx del bottone col lato sx del container (parent)
        mySet.connect(bu1.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        // collego il top del bottone col top del container -> il bottone sta in alto a sinistra
        mySet.connect(bu1.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        // positioning di bu2
        mySet.connect(bu2.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        mySet.connect(bu2.id, ConstraintSet.TOP, bu1.id, ConstraintSet.BOTTOM)

        // lato sx del text con il lato destro del button
        mySet.connect(tv.id, ConstraintSet.LEFT, bu1.id, ConstraintSet.RIGHT)
        // allineo text e button sulla stessa linea orizzontale (baseline)
        mySet.connect(tv.id, ConstraintSet.BASELINE, bu1.id, ConstraintSet.BASELINE)

        // applico i vincoli a tutto il layout (prima li specifico tutti)
        mySet.applyTo(myLayout)

        // Account for system bars insets
        myLayout.fitsSystemWindows = true // fitta l'applicazione tra la navigation bar e la notification bar

        // Enable edge-to-edge display on API level < 35
        WindowCompat.enableEdgeToEdge(window)

        // Ensure that system bars remain visible regardless of the background color
        manageSystemBarsAppearance(myLayout) // activity prende tutto lo schermo tranne le system bars

        // Display the layout
        setContentView(myLayout) // tutta la parte assegnata all'activity è occupata da myLayout

        // passare da vertical a landscape rappresenta un cambio di configurazione
        // -> in questo caso text e button si "resettano" perché non ho salvato lo stato
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
