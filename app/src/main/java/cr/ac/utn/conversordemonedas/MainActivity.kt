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
                    },
                    onGoToHistory = {
                        val intent = Intent(this, HistoryActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    onGoToConversion: () -> Unit,
    onGoToHistory: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            //Botón para ir a la conversión
            Button(onClick = onGoToConversion) {
                Text(text = stringResource(R.string.btn_go_to_conversion))
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Botón para ir al historial
            Button(onClick = onGoToHistory) {
                Text(text = "Ver historial")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // (Más adelante aquí puedes volver a activar los de cámara y galería)
        }
    }
}