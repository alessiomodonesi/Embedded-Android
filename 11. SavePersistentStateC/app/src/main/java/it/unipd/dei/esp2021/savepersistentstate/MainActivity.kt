package it.unipd.dei.esp2021.savepersistentstate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import it.unipd.dei.esp2021.savepersistentstate.ui.theme.SavePersistentStateTheme
import kotlin.math.roundToInt

/**
 * Versione dell'app implementata con Jetpack Compose
 */
class MainActivity : ComponentActivity()
{
    // Class constants
    companion object {
        // Chiavi per SharedPreferences (Persistent State)
        private const val KEY_TEXT_COLOR = "editTextValue"
        private const val KEY_TEXT_SIZE = "seekBarValue"
        private const val KEY_TEXT_ALL_CAPS = "checkBoxValue"
    }

    /* * TIP: STATE HOISTING A LIVELLO DI ACTIVITY
     * In Compose, lo stato persistente viene "sollevato" (hoisted) a livello di Activity
     * Questo è necessario perché deve essere accessibile sia da onCreate() per il ripristino,
     * sia da onPause() per il salvataggio
     */
    private val textColor = mutableStateOf("")
    private val textSize = mutableIntStateOf(0)
    private val textAllCaps = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Recupero dello stato persistente da SharedPreferences
        val preferences = getPreferences(MODE_PRIVATE)

        // Sovrascrive i valori iniziali con quelli salvati
        textColor.value = preferences.getString(KEY_TEXT_COLOR, null) ?: "FF669900"
        textSize.intValue = preferences.getInt(KEY_TEXT_SIZE, 18)
        textAllCaps.value = preferences.getBoolean(KEY_TEXT_ALL_CAPS, false)

        setContent {
            SavePersistentStateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

    /* * TIP: SALVATAGGIO STATO PERSISTENTE
     * Come per la versione Views, il salvataggio avviene in onPause()
     * Questo garantisce che i dati sopravvivano anche se l'app viene chiusa o uccisa
     */
    @SuppressLint("UseKtx")
    override fun onPause()
    {
        super.onPause()
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()

        // I dati persistenti (colore, dimensione, caps) vengono memorizzati su disco
        editor.putString(KEY_TEXT_COLOR, textColor.value)
        editor.putInt(KEY_TEXT_SIZE, textSize.intValue)
        editor.putBoolean(KEY_TEXT_ALL_CAPS, textAllCaps.value)

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
    /* * TIP: REMEMBER SAVEABLE
     * Il testo è gestito con rememberSaveable: questo è un "Transient State"
     * Sopravvive ai cambi di configurazione (es. rotazione dello schermo,
     * ma NON viene salvato nelle SharedPreferences in onPause()
     */
    var text by rememberSaveable { mutableStateOf(textState) }

    // Delega lo stato agli oggetti sollevati a livello di Activity
    var color by colorState
    var size by sizeState
    var allCaps by allCapsState

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        // Visualizzazione del testo formattato
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (allCaps) text.uppercase() else text,
                color = try { Color(Integer.parseUnsignedInt(color, 16)) }
                catch (e: Exception) { Color.Gray },
                fontSize = size.toFloat().sp
            )
        }

        // Pannello dei controlli (40% inferiore dello schermo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Riga 1: Input del testo (Stato transitorio)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(text = stringResource(R.string.text), fontSize = 18.sp, modifier = Modifier.weight(0.23f))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(0.57f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Riga 2: Colore (Stato persistente)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(text = stringResource(R.string.color), fontSize = 18.sp, modifier = Modifier.weight(0.23f))
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    modifier = Modifier.weight(0.57f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Riga 3: Dimensione (Slider - Stato persistente)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(text = stringResource(R.string.size), fontSize = 18.sp, modifier = Modifier.weight(0.23f))
                Slider(
                    value = size.toFloat(),
                    onValueChange = { size = it.roundToInt() },
                    valueRange = 14f..64f,
                    modifier = Modifier.weight(0.57f)
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }

            // Riga 4: Checkbox All Caps (Stato persistente)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(0.10f))
                Text(text = stringResource(R.string.all_caps), fontSize = 18.sp, modifier = Modifier.weight(0.23f))
                Checkbox(
                    checked = allCaps,
                    onCheckedChange = { allCaps = it },
                    modifier = Modifier.weight(0.57f).wrapContentWidth(Alignment.Start)
                )
                Spacer(modifier = Modifier.weight(0.10f))
            }
        }
    }
}
