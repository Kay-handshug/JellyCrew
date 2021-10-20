package handshug.jellycrew.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.Toast
import handshug.jellycrew.R
import org.apache.commons.io.IOUtils
import org.koin.core.context.GlobalContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    private const val PHOTO_EXTENSION = ".jpg"

    val context: Context = GlobalContext.get().koin.get()

    private fun getOutputDirectory(): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
    }

    fun getFile(uri: Uri): File {
        return File(getPathFromUri(uri) ?: "")
    }

    private fun createFile(baseFolder: File) =
        File(
            baseFolder, SimpleDateFormat(FILENAME, Locale.getDefault())
                .format(System.currentTimeMillis()) + PHOTO_EXTENSION
        )

    private fun getPathFromUri(uri: Uri): String? {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)!!

        parcelFileDescriptor.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

            val fileName = context.contentResolver.getFileName(uri)
            val fileNameType =
                try {
                    fileName.split(".")[1]
                } catch (e: Exception) {
                    ""
                }
            if(fileNameType.contains("gif")) {
                Toast.makeText(context, "toastUploadBlockGif", Toast.LENGTH_SHORT).show()
                return null
            }

            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            IOUtils.copy(inputStream, outputStream)

            return file.absolutePath
        }
    }

    fun getVideoThumbnail(uri: Uri): Bitmap? {
        val path = getPathFromUri(uri)!!
        return ThumbnailUtils.createVideoThumbnail(
            path,
            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
        )
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        val contentResolver = context.contentResolver

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    fun bitmapToFile(bitmap: Bitmap): File? {
        return try {
            val file = createFile(getOutputDirectory())

            // Convert bitmap to byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream) // YOU can also save it in JPEG
            val bitmapData = byteArrayOutputStream.toByteArray()

            // write the bytes in file
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bitmapData)
            fileOutputStream.flush()
            fileOutputStream.close()
            file
        } catch (e: Exception) {
            null // it will return null
        }
    }

    fun getBitmapFromBlob(blobContent: String?): Bitmap? {
        blobContent?.let {
            val decodedString = Base64.decode(blobContent, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
        return null
    }


    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

}