package cr.ac.utn.conversordemonedas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var usd by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val rate = 520.35

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.conversion_title), fontSize = 22.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = usd,
                onValueChange = { usd = it },
                label = { Text(stringResource(R.string.label_amount_usd)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val value = usd.toDoubleOrNull()
                if (value != null) {
                    result = "${value * rate} CRC"
                } else {
                    result = stringResource(R.string.msg_invalid_input)
                }
            }) {
                Text(stringResource(R.string.btn_convert))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = result, fontSize = 18.sp)
        }
    }
}