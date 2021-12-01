package handshug.jellycrew.utils

import android.content.Context
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.modules.ApiContract
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetUserInfoTask(con: Context) : Thread() {

    val context = con

    override fun run() {
        val requestHeaders = HashMap<String, String>()
        requestHeaders["Authorization"] = "Bearer ${Preference.socialAccessToken}"

        val con = ApiContract.API_NAVER_USER_INFO.connect()

        try {
            con.requestMethod = "GET"
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }

            val responseCode = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseBody = readBody(con.inputStream)

                val json = JSONObject(responseBody).optJSONObject("response")
                Preference.userSocialId = json?.optString("id")?: ""
                Preference.userSocialGender = json?.optString("gender")?: ""
                Preference.userSocialEmail = json?.optString("email")?: ""

                val year = json?.optString("birthyear")?: ""
                val day = json?.optString("birthday")?: ""
                val userBirth = "$year-$day"
                Preference.userSocialBirthDay = userBirth
            } else {
                readBody(con.errorStream)
            }
        } catch (e: IOException) {
            throw RuntimeException(context.getString(R.string.login_social_error_04))
        } catch (e: Exception) {
            throw RuntimeException(context.getString(R.string.login_social_error_04))
        } finally {
            con.disconnect()
        }
    }

    private fun String.connect(): HttpURLConnection {
        try {
            val url = URL(this)
            return url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException(context.getString(R.string.login_social_error_02))
        } catch (e: IOException) {
            throw RuntimeException(context.getString(R.string.login_social_error_01))
        }
    }

    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)

        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()

                var line: String? = lineReader.readLine()
                while (line != null) {
                    responseBody.append(line)
                    line = lineReader.readLine()
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException(context.getString(R.string.login_social_error_03))
        }
    }
}