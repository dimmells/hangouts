package com.example.dmelnyk.ft_hangouts.data

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.ui.CreateContactFragment
import kotlinx.android.synthetic.main.item_message_list.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoFormatter {

    fun rotateAndCrop(original: Bitmap, degrees: Float): Bitmap = getCroppedBitmap(rotateBitmap(original, degrees))

    private fun rotateBitmap(original: Bitmap, degrees: Float): Bitmap {
        val width = original.width
        val height = original.height
        val matrix = Matrix()
        matrix.preRotate(degrees)
        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, true)
    }

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f,
                bitmap.width / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun getPhoto(photoSrc: String, activity: Activity): Bitmap? {
        if (photoSrc != "default") {
            try {
                return MediaStore.Images.Media.getBitmap(activity.contentResolver, Uri.parse(photoSrc))
            } catch (e: Exception) {
                val imgFile = File(photoSrc)
                if (imgFile.exists()) {
                    return BitmapFactory.decodeFile(imgFile.absolutePath)
                }
            }
        }
        return null
    }

    fun savePhoto(myBitmap: Bitmap, context: Context, resources: Resources): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + CreateContactFragment.IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".png"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/png"), null)
            fo.close()
            Toast.makeText(context, resources.getString(R.string.add_contact_save), Toast.LENGTH_SHORT).show()
            return f.absolutePath
        }
        catch (e1: IOException) {
            Toast.makeText(context, resources.getString(R.string.chat_unexpected_error), Toast.LENGTH_SHORT).show()
        }
        return "default"
    }

}