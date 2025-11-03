package cr.ac.utn.conversordemonedas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversorDeMonedasTheme {
                MainMenuScreen(
                    onGoToConversion = {
                        val intent = Intent(this, ConversionActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(onGoToConversion: () -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido al Conversor de Monedas",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onGoToConversion) {
                Text(text = stringResource(R.string.btn_go_to_conversion))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cámara (solo interfaz, sin acción)
            Button(onClick = { /* no implementado */ }) {
                Text(text = stringResource(R.string.btn_open_camera))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de galería (solo interfaz, sin acción)
            Button(onClick = { /* no implementado */ }) {
                Text(text = stringResource(R.string.btn_open_gallery))
            }
        }
    }
}
