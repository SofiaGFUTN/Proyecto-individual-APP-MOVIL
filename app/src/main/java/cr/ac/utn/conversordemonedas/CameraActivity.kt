package cr.ac.utn.conversordemonedas

import Data.MemoryStorage
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class CameraActivity : ComponentActivity() {

    private val CAMERA_REQUEST = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val btnTake = findViewById<Button>(R.id.btnTakePhoto)
        val imgPreview = findViewById<ImageView>(R.id.imgPreviewCamera)

        btnTake.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            MemoryStorage.savedImages.add(bitmap)

            finish()
        }
    }
}
