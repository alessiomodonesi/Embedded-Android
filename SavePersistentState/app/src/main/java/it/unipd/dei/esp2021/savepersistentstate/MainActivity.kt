package it.unipd.dei.esp2021.savepersistentstate

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat


class MainActivity : AppCompatActivity()
{
    // Class constants
    companion object {
        private const val KEY_TEXT_COLOR = "editTextValue"
        private const val KEY_TEXT_SIZE = "seekBarValue"
        private const val KEY_TEXT_ALL_CAPS = "checkBoxValue"
    }

    // Class variables
    private lateinit var tv : TextView
    private lateinit var etText : EditText
    private lateinit var etColor : EditText
    private lateinit var sbSize : SeekBar
    private lateinit var cbAllCaps : CheckBox

    // Called once when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35
        WindowCompat.enableEdgeToEdge(window)

        // Display the layout
        setContentView(R.layout.activity_main)

        // Get references to UI objects
        tv = findViewById(R.id.tv)
        etText = findViewById(R.id.et_text)
        etColor = findViewById(R.id.et_color)
        sbSize = findViewById(R.id.sb_size)
        cbAllCaps = findViewById(R.id.cb_all_caps)

        // Retrieve a SharedPreferences object with data that are private to this activity
        val preferences = getPreferences(MODE_PRIVATE)

        // Set UI state according to retrieved data
        etColor.setText(preferences.getString(KEY_TEXT_COLOR, "FF669900"))
        sbSize.progress = preferences.getInt(KEY_TEXT_SIZE, 18)
        cbAllCaps.isChecked = preferences.getBoolean(KEY_TEXT_ALL_CAPS, false)

        // Set actions to be performed when the user provides new data

        // New text
        etText.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    // Update text
                    updateText()
                    // Hide keyboard
                    val imm =
                        v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    // Give up focus
                    v.clearFocus()
                    true
                }
                else -> false
            }
        }

        // New text color
        etColor.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    // Update text
                    updateText()
                    // Hide keyboard
                    val imm =
                        v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    // Give up focus
                    v.clearFocus()
                    true
                }
                else -> false
            }
        }

        // New text size
        sbSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update text
                updateText()
            }

            // OnSeekBarChangeListener is an interface,
            // so an implementation must be provided for all the methods
            override fun onStartTrackingTouch(seek: SeekBar) = Unit
            override fun onStopTrackingTouch(seek: SeekBar) = Unit
        })

        // New value for the "all caps" property
        cbAllCaps.setOnCheckedChangeListener { _, _ ->
            // Update text
            updateText()
        }

        // Ensure that system bars remain visible regardless of the background color:
        // done via XML styling
    }

    // Called every time the activity becomes active and ready to receive user input
    override fun onResume()
    {
        super.onResume()
        updateText()
    }

    // Called every time the user no longer actively interacts with the activity,
    // but it is still visible on screen. The counterpart to onResume()
    override fun onPause()
    {
        super.onPause()

        // Store values between instances here
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()

        // Store relevant status of UI objects that are part of the persistent state
        editor.putString(KEY_TEXT_COLOR, etColor.text.toString())
        editor.putInt(KEY_TEXT_SIZE, sbSize.progress)
        editor.putBoolean(KEY_TEXT_ALL_CAPS, cbAllCaps.isChecked)

        // Commit to storage synchronously
        editor.apply()
    }

    // Called to set the text attributes according to widget status
    private fun updateText()
    {
        tv.text = etText.text
        // Parse the hex ARGB color string; fall back to gray on invalid input
        tv.setTextColor(try { Integer.parseUnsignedInt(etColor.text.toString(), 16) }
                        catch(e: Exception) { Color.GRAY })
        tv.setTextSize(COMPLEX_UNIT_SP, sbSize.progress.toFloat())
        tv.isAllCaps = cbAllCaps.isChecked
    }
}
