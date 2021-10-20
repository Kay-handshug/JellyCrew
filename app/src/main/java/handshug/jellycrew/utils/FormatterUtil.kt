package handshug.jellycrew.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

object FormatterUtil {
    const val DATETIME_FORMAT_MS = "yyyy-MM-dd HH:mm:ss.SSS"
    const val DATE_FORMAT_YEAR_MONTH_DATE = "yyyy-MM-dd"
    const val DATE_FORMAT_YEAR_MONTH_DATE_KOREAN = "yyyy년 MM월 dd일"
    const val DATE_FORMAT_MONTH_DATE_KOREAN = "MM월 dd일"
    const val TIME_FORMAT_HOUR_MINUTE = "a h:mm"

    /**
     * RFC3309
     * 2016-06-08T10:00:00Z <- yyyy-MM-dd'T'HH:mm:ss'Z'
     * 2016-06-08T10:00:00+09:00 <- yyyy-MM-dd'T'HH:mm:ssZZZZZ
     * 2019-11-30T15:00:00.000Z <- yyyy-MM-dd'T'HH:mm:ss.SSSXXX
     */

    fun convertTime(date: Date?): String {
        return date?.let {
            SimpleDateFormat("a hh:mm", Locale.getDefault()).format(date).toString()
        }?: run {
            ""
        }
    }

    fun convertDateToString(date: Date?): String {
        return date?.let {
            SimpleDateFormat("MM월 dd일 (E)", Locale.getDefault()).format(date).toString()
        }?: run {
            ""
        }
    }

    fun convertDateToString(date: Date?, pattern: String): String {
        return date?.let {
            SimpleDateFormat(pattern, Locale.getDefault()).format(date).toString()
        }?: run {
            ""
        }
    }

    fun convertLocalDateTimeToString(date: LocalDateTime, pattern: String): String {
        return date.format(DateTimeFormatter.ofPattern(pattern))
    }

    fun LocalDateTime.formatted(pattern: String) = this.format(DateTimeFormatter.ofPattern(pattern))

    fun convertLocalDateToString(date: LocalDate, pattern: String): String {
        return date.format(DateTimeFormatter.ofPattern(pattern))
    }

    fun convertStringToLocalDateTime(datetimeString: String, pattern: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern(pattern))
        } catch(e: Exception) {
            null
        }
    }

    fun convertStringToLocalDate(dateString: String, pattern: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern))
        } catch(e: Exception) {
            null
        }
    }

    fun convertCommaNumber(number: Any?): String {
        return number?.let {
            return String.format("%,d", number)
        } ?: run {
            ""
        }
    }

    fun convertCommaNumber(number: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(number)
    }

    fun convertCommaNumberPlusWon(number: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return "${decimalFormat.format(number)}원"
    }

    fun convertCommaNumberPlusWon(number: Long): String {
        val decimalFormat = DecimalFormat("#,###")
        return "${decimalFormat.format(number)}원"
    }

    fun removeCommaNumber(number: String): Int {
        return if(number.isNotEmpty()) number.replace(",","").toInt() else 0
    }


    private fun getDate(date: String): Date? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).parse(date)
        } else {
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            calendar.time = format.parse(date)!!

            val utcGap = getUtcGap()

            calendar.add(Calendar.HOUR, utcGap.first)
            calendar.add(Calendar.MINUTE, utcGap.second)
            Date(calendar.timeInMillis)
        }
    }

    private fun getUtcGap(): Pair<Int, Int> {
        val utc = TimeZone.getTimeZone("UTC")
        val default = TimeZone.getDefault()

        val difference = (default.rawOffset - utc.rawOffset).toLong()
        val hourGap = TimeUnit.MILLISECONDS.toHours(difference).toInt()
        val minutesGap = TimeUnit.MILLISECONDS.toMinutes(difference).toInt()

        return Pair(hourGap, minutesGap % 60)
    }


    fun convertToString(value: Any?): String {
        return value?.toString() ?: ""
    }

    fun format(format: String, value: Any?): String {
        return value?.let {
            String.format(format, convertCommaNumber(value))
        } ?: ""
    }

    fun convertFormat(format: String, value: Any?): String {
        return String.format(format, value)
    }


    fun convertCurrencyFormat(digit: Int, price: BigDecimal?): String {
        return String.format("%,.${digit}f", price)
    }

    fun getTimeBuyTimestamp(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
    }

    fun getTodayDate(): Date {
        return Calendar.getInstance(Locale.getDefault()).time
    }

    fun getOneMonthAgoDate(): String {
        val today = Calendar.getInstance(Locale.getDefault())
        today.add(Calendar.MONTH, -1)
        return getDateFormat(today)
    }

    fun getThreeMonthAgoDate(): String {
        val today = Calendar.getInstance(Locale.getDefault())
        today.add(Calendar.MONTH, -3)
        return getDateFormat(today)
    }

    private fun getDateFormat(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    fun convertLocalDateToCalendarDay(localDate: LocalDate): CalendarDay {
        return CalendarDay.from(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun convertCalendarDayToLocalDate(calendarDay: CalendarDay): LocalDate {
        return LocalDate.of(calendarDay.year, calendarDay.month, calendarDay.day)
    }

    fun convertDateTimeStringFormat(datetimeString: String, formatFrom: String, formatTo: String): String? {
        val datetime = convertStringToLocalDateTime(datetimeString, formatFrom)
        datetime?.let {
            return convertLocalDateTimeToString(it, formatTo)
        } ?: return null
    }
}