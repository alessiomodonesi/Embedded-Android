package it.unipd.dei.esp2021.hellowithbuttonjni

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

        // Enable edge-to-edge display on API level < 35
        WindowCompat.enableEdgeToEdge(window)

        // Display the layout
        setContentView(R.layout.activity_main)

        // Get references to the TextView and the button
        val tv : TextView = findViewById(R.id.tv)
        val bu : Button = findViewById(R.id.bu)

        // Set the action to be performed when the button is pressed
        bu.setOnClickListener { // Perform action on click
            tv.text = stringFromJNI()
        }

        // Ensure that system bars remain visible regardless of the background color.
        // Must be done programmatically because it depends on the device theme at runtime
        manageSystemBarsAppearance(findViewById(R.id.cl))
    }

    // Declaration of the native stringFromJNI() method.
    // The method is implemented by the 'native-lib' native library,
    // which is packaged with this application
    private external fun stringFromJNI(): String?

    // Declaration of another native method that is not implemented
    // anywhere; trying to call it will result in a
    // java.lang.UnsatisfiedLinkError exception.
    // This is simply to show that you can declare as many native
    // methods in your Java code as you want: their implementation
    // is searched in the currently loaded native libraries only
    // the first time you call them
    private external fun unimplementedStringFromJNI(): String?

    companion object {
        // Loads the 'native-lib' library on application startup.
        // The library has already been unpacked into
        // /data/data/it.unipd.dei.esp2021.hellowithbuttonjni/lib/
        // at installation time by the package manager.
        init {
            System.loadLibrary("native-lib")
        }
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
