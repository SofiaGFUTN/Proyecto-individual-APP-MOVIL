package Util

import android.content.Context
import android.widget.Toast

object Util {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun formatResult(value: Double): String {
        return String.format("%.2f", value)
    }
}