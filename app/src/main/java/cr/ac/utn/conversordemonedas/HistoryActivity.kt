package cr.ac.utn.conversordemonedas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversorDeMonedasTheme {
                HistoryScreen(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(context: Context) {
    // Cargar historial guardado en SharedPreferences
    val prefs = context.getSharedPreferences("conversion_history", Context.MODE_PRIVATE)
    val savedHistory =
        prefs.getStringSet("records", emptySet())?.toMutableList() ?: mutableListOf()

    val historyList = remember {
        mutableStateListOf<String>().apply { addAll(savedHistory) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Historial de conversiones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (historyList.isEmpty()) {
                Text(
                    text = "No hay conversiones recientes",
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(historyList) { conversion ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Text(
                                text = conversion,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // Limpiar historial tanto en pantalla como en memoria
                        historyList.clear()
                        prefs.edit().remove("records").apply()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Borrar todo")
                }
            }
        }
    }
}
