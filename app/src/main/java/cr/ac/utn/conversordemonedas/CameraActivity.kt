package cr.ac.utn.conversordemonedas

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnTakePhoto: Button
    private lateinit var btnUpload: Button
    private lateinit var btnBack: Button

    private var photoUri: Uri? = null

    //Permission
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePhoto()
        } else {
            Toast.makeText(
                this,
                getString(R.string.camera_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Take a photo
    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            imageView.setImageURI(photoUri)
            btnUpload.isEnabled = true
        }
    }
    //Variables and function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        imageView = findViewById(R.id.imageView)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnUpload = findViewById(R.id.btnUpload)
        btnBack = findViewById(R.id.btnBack)

        btnTakePhoto.setOnClickListener {
            checkPermissionAndTakePhoto()
        }

        btnUpload.setOnClickListener {
            photoUri?.let { uri ->
                uploadPhotoToFirebase(uri)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    //Check permission and photo
    private fun checkPermissionAndTakePhoto() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            else -> {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun takePhoto() {
        try {
            //Create file for the photo
            val photoFile = File(
                getExternalFilesDir(null),
                "photo_${System.currentTimeMillis()}.jpg"
            )

            //Converts a file path to a URI
            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )

            photoUri = uri
            takePicture.launch(uri)

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //The photo is uploaded to Firebase
    private fun uploadPhotoToFirebase(uri: Uri) {
        btnUpload.isEnabled = false
        btnTakePhoto.isEnabled = false

        val fileName = "camera_${System.currentTimeMillis()}.jpg"
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("camera_photos/$fileName")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    getString(R.string.camera_upload_success),
                    Toast.LENGTH_SHORT
                ).show()

                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    android.util.Log.d("CAMERA", "URL: $imageUrl")

                    val prefs = getSharedPreferences("images", MODE_PRIVATE)
                    prefs.edit().putString("last_camera_photo", imageUrl).apply()
                }

                btnUpload.isEnabled = false
                btnTakePhoto.isEnabled = true
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.camera_upload_error),
                    Toast.LENGTH_LONG
                ).show()

                btnUpload.isEnabled = true
                btnTakePhoto.isEnabled = true
            }
    }
}