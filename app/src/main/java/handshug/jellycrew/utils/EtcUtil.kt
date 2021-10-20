package handshug.jellycrew.utils

import android.telephony.SmsManager
import android.util.Base64
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import handshug.jellycrew.TimeSynchronizer
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

    fun LocalDate.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.of(this, LocalTime.MIN)
    }

    fun getDateTime(): LocalDateTime {
        return TimeSynchronizer.getSyncedLocalDateTime()
    }

    fun getDateTimeFromTicks(ticks: Long): LocalDateTime {
        return LocalDateTime.of(1970,1,1,9,0).plusSeconds(ticks)
    }

    fun getDate(): LocalDate {
        return getDateTime().toLocalDate()
    }

    fun getMinDateTime(): LocalDateTime {
        return LocalDateTime.of(2020,1,1,0,0,0,0)
    }

    fun getCurrentTimeMinuteTicks(): Long {
        return ChronoUnit.MINUTES.between(LocalDateTime.of(1970, 1, 1, 9, 0), getDateTime())
    }

    fun getCurrentTimeMSTicks(): Long {
        return ChronoUnit.MILLIS.between(LocalDateTime.of(1970, 1, 1, 9, 0), getDateTime())
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
}