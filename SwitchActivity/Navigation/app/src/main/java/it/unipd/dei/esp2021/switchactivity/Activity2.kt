package it.unipd.dei.esp2021.switchactivity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class Activity2 : AppCompatActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        setContentView(R.layout.activity_2)

        val previous : Button = findViewById(R.id.Button2)
        previous.setOnClickListener {
            // Note: no result is communicated to the caller
            finish()
        }

        // Ensure that system bars remain visible regardless of the background color:
        // can be done via XML styling because minSdk is 27
    }
}
