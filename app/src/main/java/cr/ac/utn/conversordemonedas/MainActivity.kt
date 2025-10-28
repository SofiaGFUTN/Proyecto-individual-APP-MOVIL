package cr.ac.utn.conversordemonedas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConversorDeMonedasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ConversorDeMonedas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConversorDeMonedas(modifier: Modifier = Modifier) {
    var monto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var tipoCambio by remember { mutableStateOf(525.0) } // Ejemplo: 1 USD = 525 CRC

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Conversor de Monedas 💱",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto en USD") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val valor = monto.toDoubleOrNull()
                if (valor != null) {
                    val calculo = valor * tipoCambio
                    resultado = "₡ %.2f".format(calculo)
                } else {
                    resultado = "Por favor, ingresa un número válido"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir a colones")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = resultado,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConversorDeMonedasPreview() {
    ConversorDeMonedasTheme {
        ConversorDeMonedas()
    }
}
