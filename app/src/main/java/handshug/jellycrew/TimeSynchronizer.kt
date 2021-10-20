package handshug.jellycrew

import com.lyft.kronos.AndroidClockFactory
import com.lyft.kronos.KronosClock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import handshug.jellycrew.Preference.context


object TimeSynchronizer {
    private var synchronizer: KronosClock? = null
    private var syncServerTimeGap: Long = 0

    fun sync() {
        synchronizer = AndroidClockFactory.createKronosClock(context = context, ntpHosts = listOf("time.windows.com"))
        synchronizer?.syncInBackground()
    }

    fun release() {
        if (synchronizer != null) {
            synchronizer?.shutdown()
            synchronizer = null
        }
    }

    fun getSyncedLocalDateTime(): LocalDateTime {
        val time = try {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(getTimeMS()), ZoneId.systemDefault())
        } catch (e: NTPDisconnectedException) {
            LocalDateTime.now()
        }
        return time
    }

    private fun getTimeMS(): Long {
        return synchronizer?.getCurrentNtpTimeMs()?.let{
            it - syncServerTimeGap
        } ?: throw NTPDisconnectedException()
    }

    @JvmStatic
    fun calculateTimeGap(syncServerTimeMS: Long) {
        syncServerTimeGap = getTimeMS() - syncServerTimeMS
    }

    class NTPDisconnectedException: Exception()
}