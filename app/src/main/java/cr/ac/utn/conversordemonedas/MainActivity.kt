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
                    // Las siguientes están comentadas porque aún no usas cámara ni galería
                    /*
                    onGoToCamera = {
                        val intent = Intent(this, CameraActivity::class.java)
                        startActivity(intent)
                    },
                    onGoToGallery = {
                        val intent = Intent(this, GalleryActivity::class.java)
                        startActivity(intent)
                    }
                    */
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    onGoToConversion: () -> Unit,
    //onGoToCamera: () -> Unit,
    //onGoToGallery: () -> Unit
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

            Button(onClick = onGoToConversion) {
                Text(text = "Ir a conversión")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de cámara y galería
            /*
            Button(onClick = onGoToCamera) {
                Text(text = stringResource(R.string.btn_open_camera))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onGoToGallery) {
                Text(text = stringResource(R.string.btn_open_gallery))
            }
            */
        }
    }
}
