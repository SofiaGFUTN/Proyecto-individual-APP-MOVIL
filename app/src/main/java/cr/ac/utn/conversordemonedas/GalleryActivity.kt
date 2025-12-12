package cr.ac.utn.conversordemonedas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class GalleryActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var btnUpload: Button
    private lateinit var btnBack: Button

    private var selectedImageUri: Uri? = null

    //Image selection and saving
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri

            val bitmap = uriToBitmap(uri)
            imgPreview.setImageBitmap(bitmap)

            btnUpload.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        imgPreview = findViewById(R.id.imgPreviewGallery)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnUpload = findViewById(R.id.btnUpload)
        btnBack = findViewById(R.id.btnBack)

        btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnUpload.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadImageToFirebase(uri)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun uploadImageToFirebase(uri: Uri) {
        btnUpload.isEnabled = false
        btnPickImage.isEnabled = false

        //Create a unique name for the image and upload the image
        val fileName = "gallery_${System.currentTimeMillis()}.jpg"
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("gallery_images/$fileName")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    getString(R.string.gallery_upload_success),
                    Toast.LENGTH_SHORT
                ).show()

                //Show URL to download
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    android.util.Log.d("GALLERY", "URL: $imageUrl")

                    val prefs = getSharedPreferences("images", MODE_PRIVATE)
                    prefs.edit().putString("last_gallery_image", imageUrl).apply()
                }

                btnUpload.isEnabled = false
                btnPickImage.isEnabled = true
            }
            //Show error
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.gallery_upload_error),
                    Toast.LENGTH_LONG
                ).show()

                btnUpload.isEnabled = true
                btnPickImage.isEnabled = true
            }
    }

    //Converts from URI to bitmap
    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        }
    }
}