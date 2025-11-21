package cr.ac.utn.conversordemonedas

import Data.MemoryStorage
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class GalleryActivity : ComponentActivity() {

    private val GALLERY_REQUEST = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val btnPick = findViewById<Button>(R.id.btnPickImage)
        val imgPreview = findViewById<ImageView>(R.id.imgPreviewGallery)

        btnPick.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            MemoryStorage.savedImages.add(bitmap)

            finish()
        }
    }
}
