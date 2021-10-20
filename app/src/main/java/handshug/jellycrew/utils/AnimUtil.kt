package handshug.jellycrew.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import handshug.jellycrew.R

object AnimUtil {

    fun animFadeIn(context: Context, view: View, isRepeat: Boolean) {
        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        if(isRepeat) {
            fadeIn.repeatCount = Animation.INFINITE
            fadeIn.repeatMode = Animation.RESTART
        }
        view.startAnimation(fadeIn)
    }

    fun animFadeOut(context: Context, view: View, isRepeat: Boolean) {
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        if(isRepeat) {
            fadeOut.repeatCount = Animation.INFINITE
            fadeOut.repeatMode = Animation.RESTART
        }
        view.startAnimation(fadeOut)
    }

    fun animColorChange(view: View, from: Int, to: Int) {
        val animColor = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
        animColor.duration = 500
        animColor.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }
        animColor.start()
    }
}