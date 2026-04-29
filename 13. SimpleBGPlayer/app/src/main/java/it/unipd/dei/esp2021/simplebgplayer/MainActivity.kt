package it.unipd.dei.esp2021.simplebgplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import it.unipd.dei.esp2021.simplebgplayer.ui.theme.SimpleBGPlayerTheme

class MainActivity: ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val onPlayClick: () -> Unit = {
            val i = Intent(this, PlayerService::class.java)
            i.putExtra(PlayerService.PLAY_START, true)
            startService(i)
        }

        val onStopClick: () -> Unit = {
            val i = Intent(this, PlayerService::class.java)
            stopService(i)
        }

        setContent {
            SimpleBGPlayerTheme {
                // Reference: https://developer.android.com/develop/ui/compose/components/scaffold
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlayerScreen(
                        playAction = onPlayClick,
                        stopAction = onStopClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }

        // API level >= 33: ask notification runtime permission, if not already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionName = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permissionName) != PermissionChecker.PERMISSION_GRANTED)
            {
                Log.i(TAG, "$permissionName: asking...")
                val permissionRequest = registerForActivityResult(
                    ActivityResultContracts.RequestPermission()) { isGranted ->
                    Log.i(TAG, "$permissionName granted: $isGranted")
                }
                permissionRequest.launch(permissionName)
            }
            else
                Log.i(TAG, "$permissionName: already granted")
        }
    }

    companion object
    {
        // Logcat tag
        private val TAG = MainActivity::class.simpleName
    }
}

@Composable
fun PlayerScreen(playAction: () -> Unit, stopAction: () -> Unit,
                 modifier: Modifier = Modifier)
{
    // References:
    // https://developer.android.com/jetpack/compose/state
    // https://developer.android.com/develop/ui/compose/state-saving
    var playClickable by rememberSaveable { mutableStateOf(true) }
    var stopClickable by rememberSaveable { mutableStateOf(false) }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(modifier = modifier) {
        val (tv, buPlay, buStop) = createRefs()

        Text(
            stringResource(R.string.greetings),
            fontSize = 24.sp,
            modifier = Modifier.constrainAs(tv) {
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    start = parent.start,
                    end = parent.end,
                    verticalBias = 0.15f
                )
            }
        )

        // Play button: starts the playback music service
        Button(
            onClick = {
                playClickable = false
                playAction()
                stopClickable = true
            },
            modifier = Modifier.constrainAs(buPlay) {
                top.linkTo(tv.bottom, margin = 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = playClickable
        ) {
            Text(stringResource(R.string.button_play))
        }

        // Stop button: stops the music by stopping the service
        Button(
            onClick = {
                stopClickable = false
                stopAction()
                playClickable = true
            },
            modifier = Modifier.constrainAs(buStop) {
                top.linkTo(buPlay.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = stopClickable
        ) {
            Text(stringResource(R.string.button_stop))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    PlayerScreen(playAction = {}, stopAction = {})
}
