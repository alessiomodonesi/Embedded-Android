package it.unipd.dei.esp2021.manageorientation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import it.unipd.dei.esp2021.manageorientation.ui.theme.ManageOrientationTheme

class MainActivity: ComponentActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display on API level < 35
        enableEdgeToEdge()

        // Set and display the UI content
        setContent {
            ManageOrientationTheme {
                // Reference: https://developer.android.com/develop/ui/compose/components/scaffold
                // The scaffold fills the whole display area
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // MainScreen consumes the insets
                    // to keep the app UI away from the system UI and display cutouts
                    MainScreen(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier)
{
    val orientation = LocalConfiguration.current.orientation

    // Reference: https://developer.android.com/develop/ui/compose/state-saving
    var c1 by rememberSaveable { mutableStateOf(false) }
    var c2 by rememberSaveable { mutableStateOf(false) }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(modifier = modifier) {
        val (sw1, tv, sw2) = createRefs()

        Switch(
            checked = c1,
            onCheckedChange = { c1 = it },
            modifier = Modifier.constrainAs(sw1) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    end.linkTo(tv.start)
                    bottom.linkTo(parent.bottom)
                }
                else {
                    end.linkTo(parent.end)
                    bottom.linkTo(tv.top)
                }
            }
        )

        Text(
            text = if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                       stringResource(R.string.land)
                   else
                       stringResource(R.string.port),
            modifier = Modifier.constrainAs(tv) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        Switch(
            checked = c2,
            onCheckedChange = { c2 = it },
            modifier = Modifier.constrainAs(sw2) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    start.linkTo(tv.end)
                    top.linkTo(parent.top)
                }
                else {
                    start.linkTo(parent.start)
                    top.linkTo(tv.bottom)
                }
            }
        )
    }
}

// Reference: https://developer.android.com/develop/ui/compose/tooling/previews
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
