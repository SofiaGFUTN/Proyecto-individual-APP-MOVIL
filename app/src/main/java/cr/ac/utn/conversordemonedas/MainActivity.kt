package cr.ac.utn.conversordemonedas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartConversion: Button
    private lateinit var btnHistory: Button
    private lateinit var btnOpenCamera: Button
    private lateinit var btnOpenGallery: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        btnStartConversion = findViewById(R.id.btnStartConversion)
        btnHistory = findViewById(R.id.btnHistory)
        btnOpenCamera = findViewById(R.id.btnOpenCamera)
        btnOpenGallery = findViewById(R.id.btnOpenGallery)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        //Button to go to conversion
        btnStartConversion.setOnClickListener {
            val intent = Intent(this, ConversionActivity::class.java)
            startActivity(intent)
        }

        //Button to view history
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        //Button to open camera
        btnOpenCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        //Button to open gallery
        btnOpenGallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }
}