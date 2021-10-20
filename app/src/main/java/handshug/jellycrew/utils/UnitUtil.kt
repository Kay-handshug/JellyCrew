package handshug.jellycrew.utils

import android.content.Context
import android.util.TypedValue
import org.koin.core.context.GlobalContext

fun Int.KB(): Long {
    return (this * 1024).toLong()
}


fun Int.MB(): Long {
    return (this * 1024 * 1024).toLong()
}


fun Int.GB(): Long {
    return (this * 1024 * 1024 * 1024).toLong()
}

fun pxToDp(context: Context, px: Int): Int {
    var density = context.resources.displayMetrics.density

    when (density) {
        1.0f -> density *= 4.0f    // mdpi (160dpi)
        1.5f -> density *= (8 / 3) // hdpi (240dpi)
        2.0f -> density *= 2.0f    // xhdpi (320dpi)
    }

    val value = (px / density).toInt()

    return value
}

val Int.dp: Int
    get() {
        val context: Context = GlobalContext.get().koin.get()
        val metrics = context.resources.displayMetrics

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

fun dpToPx(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, density).toInt()
}