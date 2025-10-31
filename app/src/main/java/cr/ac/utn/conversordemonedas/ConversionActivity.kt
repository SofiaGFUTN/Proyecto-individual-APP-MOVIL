package cr.ac.utn.conversordemonedas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class ConversionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversorDeMonedasTheme {
                ConversionScreen()
            }
        }
    }
}

@Composable
fun ConversionScreen() {
    var amount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("CRC") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Conversor de Monedas", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Cantidad") },
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(value = fromCurrency, onValueChange = { fromCurrency = it }, label = { Text("De") })
                OutlinedTextField(value = toCurrency, onValueChange = { toCurrency = it }, label = { Text("A") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                result = try {
                    val value = amount.toDouble()
                    val conversion = value * 540.0 // ejemplo USD→CRC
                    "Resultado: $conversion ₡"
                } catch (e: Exception) {
                    "Error: cantidad inválida"
                }
            }) {
                Text(text = stringResource(R.string.btn_convert))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
