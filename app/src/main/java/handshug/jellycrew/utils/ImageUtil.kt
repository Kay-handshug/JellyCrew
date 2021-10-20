package handshug.jellycrew.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object ImageUtil {

    fun setImageCircle(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).centerCrop()
            .apply(RequestOptions.circleCropTransform()).into(imageView)
    }

    fun setImageCircleFit(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).centerInside()
            .apply(RequestOptions.circleCropTransform()).into(imageView)
    }

    fun setImage(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).centerCrop().into(imageView)
    }

    fun setImageOriginal(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).into(imageView)
    }

    fun setImageForReview(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).fitCenter().into(imageView)
    }

    fun setImageWithScale(imageView: ImageView, url: String, width: Int, height: Int) {
        Glide.with(imageView).load(url).override(width, height).centerCrop().into(imageView)
    }

    fun setLongImage(imageView: ImageView, url: String) {
        Glide.with(imageView)
            .load(url)
            .fitCenter()
            .diskCacheStrategy(ALL)
            .into(imageView)
    }

    @Throws(IOException::class)
    fun drawableFromUrl(url: String?): Drawable? {
        val x: Bitmap
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        val input: InputStream = connection.inputStream
        x = BitmapFactory.decodeStream(input)
        return BitmapDrawable(Resources.getSystem(), x)
    }
}
