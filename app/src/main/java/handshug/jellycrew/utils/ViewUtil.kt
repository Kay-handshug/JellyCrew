package handshug.jellycrew.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import handshug.jellycrew.R

object ViewUtil {

    fun setTextColor(view: TextView, colorId: Int) {
        view.setTextColor(ContextCompat.getColor(view.context, colorId))
    }

    fun setBackgroundColor(view: TextView, colorId: Int) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorId))
    }

    fun setBackgroundColor(view: View, colorId: Int) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorId))
    }

    fun setBackgroundDrawable(view: ConstraintLayout, drawableId: Int) {
        view.background = ContextCompat.getDrawable(view.context, drawableId)
    }

    fun setBackgroundDrawable(view: LinearLayout, drawableId: Int) {
        view.background = ContextCompat.getDrawable(view.context, drawableId)
    }

    fun setBackgroundDrawable(view: TextView, drawableId: Int) {
        view.background = ContextCompat.getDrawable(view.context, drawableId)
    }

    fun setImageDrawable(view: ImageView, drawableId: Int) {
        view.setImageDrawable(ContextCompat.getDrawable(view.context, drawableId))
    }

    fun setImage(view: ImageView, resource: Int) {
        view.setImageResource(resource)
    }

    fun setTextAppearance(view: TextView, resource: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            view.setTextAppearance(resource)
        } else {
            view.setTextAppearance(view.context, resource)
        }
    }

    fun setTextAppearance(view: RadioButton, resource: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            view.setTextAppearance(resource)
        } else {
            view.setTextAppearance(view.context, resource)
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun getColorState(context: Context, resource: Int): ColorStateList {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            context.resources.getColorStateList(resource, context.theme)
        } else {
            context.resources.getColorStateList(resource)
        }
    }

    private fun getColorSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }
    fun setHtmlText(text1: String, text2: String, color1: String, color2: String): Spanned? {
        val result1 = getColorSpanned(text1, color1)
        val result2 = getColorSpanned(text2, color2)
        return Html.fromHtml("$result1 $result2")
    }
}