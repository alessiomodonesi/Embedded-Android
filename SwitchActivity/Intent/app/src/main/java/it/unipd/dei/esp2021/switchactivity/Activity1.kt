package it.unipd.dei.esp2021.switchactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

class Activity1: ComponentActivity()
{
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwitchActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenOne(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        buttonAction = {
                            val myIntent = Intent(this, Activity2::class.java)

                            // Start Activity2. No information will be received when Activity2 exits
                            startActivity(myIntent)

                            // Note: if you need more control over the starting process,
                            // consider the alternative method startActivity(Intent!,Bundle?).
                            // If you need to pass data to the starting activity,
                            // consider adding them as intent extras.
                            // If you need to receive a result from the starting activity,
                            // look up https://developer.android.com/training/basics/intents/result
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenOne(modifier: Modifier = Modifier, buttonAction: () -> Unit)
{
    Column(modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.this_is_1),
            modifier = Modifier.padding(top = 24.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = buttonAction
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}

// Reference: https://developer.android.com/develop/ui/compose/tooling/previews
@Preview(showBackground = true)
@Composable
fun ScreenOnePreview() {
    ScreenOne(buttonAction = {})
}
