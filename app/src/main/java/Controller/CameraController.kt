package Controller

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher

class CameraController {

    fun openCamera(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcher.launch(intent)
    }

    fun openGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }
}