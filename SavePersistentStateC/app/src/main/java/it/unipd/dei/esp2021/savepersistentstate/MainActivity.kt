package it.unipd.dei.esp2021.savepersistentstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import it.unipd.dei.esp2021.savepersistentstate.ui.theme.SavePersistentStateTheme
import kotlin.math.roundToInt


class MainActivity : ComponentActivity()
{
    // Class constants
    companion object {
        private const val KEY_TEXT_COLOR = "editTextValue"
        private const val KEY_TEXT_SIZE = "seekBarValue"
        private const val KEY_TEXT_ALL_CAPS = "checkBoxValue"
    }

    // Persistent UI state, hoisted at Activity level so onCreate() and onPause() can access it.
    // Initialization values are irrelevant because they will be overwritten in onCreate()
    private val textColor = mutableStateOf("")
    private val textSize = mutableIntStateOf(0)
    private val textAllCaps = mutableStateOf(false)

    // Called once when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35
        enableEdgeToEdge()

        // Retrieve a SharedPreferences object with data that are private to this activity
        val preferences = getPreferences(MODE_PRIVATE)

        // Set UI state according to retrieved data
        textColor.value = preferences.getString(KEY_TEXT_COLOR, null) ?: "FF669900"
        textSize.intValue = preferences.getInt(KEY_TEXT_SIZE, 18)
        textAllCaps.value = preferences.getBoolean(KEY_TEXT_ALL_CAPS, false)

        // Set and display the UI content
        setContent {
            SavePersistentStateTheme {
                // The scaffold fills the whole display area
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // MainScreen consumes the insets
                    // to keep the app UI away from the system UI and display cutouts
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        textState = getString(R.string.default_text),
                        colorState = textColor,
                        sizeState = textSize,
                        allCapsState = textAllCaps
                    )
                }
            }
        }
    }

    // Called every time the user no longer actively interacts with the activity,
    // but it is still visible on screen
    override fun onPause()
    {
        super.onPause()

        // Store values between instances here
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()

        // Store relevant status of UI objects that are part of the persistent state
        editor.putString(KEY_TEXT_COLOR, textColor.value)
        editor.putInt(KEY_TEXT_SIZE, textSize.intValue)
        editor.putBoolean(KEY_TEXT_ALL_CAPS, textAllCaps.value)

        // Commit to storage synchronously
        editor.apply()
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    textState: String,
    colorState: MutableState<String>,
    sizeState: MutableState<Int>,
    allCapsState: MutableState<Boolean>
)
{
    // Transient state: the display text is not persisted to SharedPreferences
    // (but it is persisted across configuration changes)
    var text by rememberSaveable { mutableStateOf(textState) }
    // Persistent state: delegate directly to Activity-level MutableState objects
    var color by colorState
    var size by sizeState
    var allCaps by allCapsState

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        // Top 60% of the screen: display the styled text, centered
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (allCaps) text.uppercase() else text,
                // Parse the hex ARGB color string; fall back to gray on invalid input
                color = try { Color(Integer.parseUnsignedInt(color, 16)) }
                        catch (e: Exception) { Color.Gray },
                fontSize = size.toFloat().sp
            )
        }

        // Bottom 40% of the screen: four control rows
        // Row proportions: 10% space + 23% text + 57% input fields + 10% space
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Row 1: text input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(
                    text = stringResource(R.string.text),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(0.23f)
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(0.57f),
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.hint_text)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Row 2: color input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(
                    text = stringResource(R.string.color),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(0.23f)
                )
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    modifier = Modifier.weight(0.57f),
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.hint_color)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Row 3: size slider (range 14..64)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(
                    text = stringResource(R.string.size),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(0.23f)
                )
                Slider(
                    value = size.toFloat(),
                    onValueChange = { size = it.roundToInt() },
                    valueRange = 14f..64f,
                    modifier = Modifier.weight(0.57f)
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Row 4: all-caps checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(
                    text = stringResource(R.string.all_caps),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(0.23f)
                )
                Checkbox(
                    checked = allCaps,
                    onCheckedChange = { allCaps = it },
                    modifier = Modifier
                        .weight(0.57f)
                        .wrapContentWidth(Alignment.Start)
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }
        }
    }
}
