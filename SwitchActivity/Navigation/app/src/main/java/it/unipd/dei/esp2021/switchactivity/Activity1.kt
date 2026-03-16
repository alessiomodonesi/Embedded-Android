package it.unipd.dei.esp2021.switchactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class Activity1 : AppCompatActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        setContentView(R.layout.activity_1)

        if (savedInstanceState == null)
        {
            val fragment = Activity1Fragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_1, fragment)
                .commit()
        }

        // Ensure that system bars remain visible regardless of the background color:
        // can be done via XML styling because minSdk is 27
    }
}
