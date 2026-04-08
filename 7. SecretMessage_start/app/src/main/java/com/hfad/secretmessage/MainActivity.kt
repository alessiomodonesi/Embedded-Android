package com.hfad.secretmessage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.secretmessage.ui.theme.SecretMessageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecretMessageTheme {
                val navController = rememberNavController()
                var message: String = "";
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "welcome",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("welcome") {
                            WelcomeScreen(
                                onStartClicked = { navController.navigate("message") }
                            )
                        }

                        composable("message") {
                            MessageScreen { msg ->
                                message = msg
                                navController.navigate("encrypt") {
                                    popUpTo("welcome")
                                }
                                // il 2o parametro è il builder, che fornisce le opzioni di navigazione
                                // con la sintassi kotlin lo inserisco come parametro della funzione
                            }
                        }

                        composable("encrypt") {
                            EncryptScreen(messageCrypt = message)
                        }
                    }
                }
            }
        }
    }
}
