package it.unipd.dei.esp2021.switchactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unipd.dei.esp2021.switchactivity.ui.theme.SwitchActivityTheme

class Activity2: ComponentActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwitchActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenTwo(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        buttonAction = {
                            this.finish()

                            // Note: if you need to send results back to the calling activity, look up
                            // https://developer.android.com/reference/android/app/Activity#starting-activities-and-getting-results
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenTwo(modifier: Modifier = Modifier, buttonAction: () -> Unit)
{
    var c by remember { mutableStateOf(false) }

    Column(modifier = modifier,
           verticalArrangement = Arrangement.spacedBy(16.dp),
           horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.this_is_2),
            modifier = Modifier.padding(top = 24.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = buttonAction
        ) {
            Text(text = stringResource(R.string.previous))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.check_me))
            Checkbox(
                checked = c,
                onCheckedChange = { c = it }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenTwoPreview() {
    SwitchActivityTheme {
        ScreenTwo(buttonAction = {})
    }
}
