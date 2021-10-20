package handshug.jellycrew.utils

import android.util.Log
import androidx.databinding.library.BuildConfig
import com.google.gson.GsonBuilder

object Log {

    fun msg(msg: String) {
        if(BuildConfig.DEBUG) Log.e("TEST", msg)
    }

    fun json(msg: String, data: Any) {
        if(BuildConfig.DEBUG) {
            longMsg(msg, GsonBuilder().create().toJson(data))
        }
    }

    private fun longMsg(msg: String, data: String) {
        if(BuildConfig.DEBUG) {
            if(data.length > 4000) {
                msg("$msg ${data.substring(0, 4000)}")
                longMsg(msg, data.substring(4000))
            } else
                msg("$msg $data")
        }
    }
}