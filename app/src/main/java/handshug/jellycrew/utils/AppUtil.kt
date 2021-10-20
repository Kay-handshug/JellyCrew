package handshug.jellycrew.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * View Visibility
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleIf(condition: Boolean) {
    if (condition)
        this.visibility = View.VISIBLE
}

fun String.subStringFromEnd(amount: Int): String {
    if (this.length < amount)
        return this

    return this.substring(this.length - amount, this.length)
}

/**
 * View Inflate할때 필요한 핼퍼 함수
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}