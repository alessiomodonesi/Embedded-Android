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
    // Class variables
    private lateinit var tv : TextView
    private lateinit var bu : Button
    private lateinit var bu2 : Button

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35.
        // Must be done programmatically because it depends on the Android version at runtime
        WindowCompat.enableEdgeToEdge(window)

        // Display the layout
        setContentView(R.layout.activity_main)

        // Get references to UI objects
        // Do it AFTER setContentView()! Before setContentView()
        // the objects have not been instantiated yet
        // Once and for all: Kotlin synthetics are not a recommended practice
        // (https://proandroiddev.com/the-argument-over-kotlin-synthetics-735305dd4ed0)
        tv = findViewById(R.id.tv)
        bu = findViewById(R.id.bu)
        bu2 = findViewById(R.id.bu2)

        // Restore TextView state from the saved instance state
        if (savedInstanceState != null) {
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
        // Must be done programmatically because it depends on the device theme at runtime
        manageSystemBarsAppearance(findViewById(R.id.cl))
    }

    // Called when the system is about to kill the activity. This method
    // allows you to save any dynamic INSTANCE state of the activity
    // into the given Bundle, to be later received in onCreate(Bundle)
    // when the activity will be re-created.
    // Note: PERSISTENT state (which is different from instance state!)
    // should be saved in the onPause() method because onSaveInstanceState()
    // is not part of the life cycle callbacks, hence it will not be called
    // in every situation
    override fun onSaveInstanceState(outState: Bundle)
    {
        // Note: with the implementation of this method inherited from
        // AppCompatActivity, some widgets save their state in the bundle
        // by default.
        // Once the user interface contains AT LEAST one non-autosaving
        // element, you should provide a custom implementation of the method
        super.onSaveInstanceState(outState)
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
