package handshug.jellycrew.utils

import android.util.Base64
import handshug.jellycrew.Preference
import org.json.JSONObject
import java.io.UnsupportedEncodingException

object JwtUtil {

    @Throws(Exception::class)
    fun decoded(JWTEncoded: String, isAccessToken: Boolean) {
        try {
            val split = JWTEncoded.split(".")
            Log.msg("# JWT Header: " + getJson(split[0]))
            Log.msg("# JWT Body: " + getJson(split[1]))

            val jsonData = getJson(split[1])
            val json = JSONObject(jsonData)

            if (isAccessToken) {
                Preference.accessTokenExpiredAt = json.getLong("exp")
                Log.msg("# JWT accessTokenExp : ${Preference.accessTokenExpiredAt}")
            }
            else {
                Preference.refreshTokenExpiredAt = json.getLong("exp")
                Log.msg("# JWT refreshTokenExp : ${Preference.refreshTokenExpiredAt}")
            }
        } catch (e: UnsupportedEncodingException) {
            Log.msg("# JWT Error : $e")
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }
}