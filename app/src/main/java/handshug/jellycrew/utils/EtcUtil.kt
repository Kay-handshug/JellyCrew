package handshug.jellycrew.utils

import android.telephony.SmsManager
import android.util.Base64
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import handshug.jellycrew.Preference
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.api.member.MemberLoginResponse
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object EtcUtil {

    fun checkRemainingAmountSum(inputPrice: String, remainPrice: String): Int {
        val input = FormatterUtil.removeCommaNumber(inputPrice)
        val remain = FormatterUtil.removeCommaNumber(remainPrice)

        return input.plus(remain)
    }

    fun sendMessage(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) { }
    }

    fun getDateTime(): LocalDateTime {
        return TimeSynchronizer.getSyncedLocalDateTime()
    }

    fun getBase64UrlSafeEncodedLongValue(l: Long): String {
        val buffer = ByteBuffer.allocate(8) // size of long
        buffer.putLong(l)
        val base64EncodedLongValue = Base64.encodeToString(buffer.array(), Base64.URL_SAFE)
        return base64EncodedLongValue.substring(0, base64EncodedLongValue.length - 1) // remove last character because it is always '\n'
    }

    fun decodeBase64UrlSafeEncodedLongValue(encodedNid: String?): Long {
        val decodedBuffer = ByteBuffer.wrap(Base64.decode(encodedNid, Base64.URL_SAFE))
        return decodedBuffer.long
    }

    fun getBase64EncodedNid(nid: Long): String {
        val base64EncodedValue = getBase64UrlSafeEncodedLongValue(nid)
        return base64EncodedValue.substring(0, base64EncodedValue.length -1)
    }

    fun getGenderType(gender: Int): String {
        return when(gender) {
            0 -> "W"
            1 -> "M"
            else -> "N"
        }
    }

    fun getUserSocialType(): String {
        return when(Preference.loginType) {
            1 -> "KAKAO"
            2 -> "NAVER"
            3 -> "FACEBOOK"
            else -> ""
        }
    }

    fun login(result: MemberLoginResponse) {
        Preference.apply {
            val token = result.token
            userId = token.accountId
            accessToken = token.access_token
            refreshToken = token.refresh_token
        }
    }

    fun logout() {
        Preference.isLogin = false
        Preference.userPassword = ""
        Preference.userSocialEmail = ""
        Preference.userSocialId = ""
        Preference.accessToken = ""
        Preference.refreshToken = ""
    }

    fun joinEmail() {
        Preference.apply {
            userSocialId = ""
            userSocialName = ""
            userSocialEmail = ""
            userSocialBirthDay = ""
            userSocialPhoto = ""
            userSocialNickname = ""
            userSocialGender = ""
            userSocialPhoneNumber = ""
            userSocialType = ""

            socialAccessToken = ""
            socialAccessTokenExpiredAt = -1L
            socialRefreshToken = ""
            socialRefreshTokenExpiredAt = -1L

            loginType = 0
        }
    }
}