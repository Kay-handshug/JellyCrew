package handshug.jellycrew.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import handshug.jellycrew.Preference
import java.util.*

object DeviceUtil {

    fun getDeviceModel(): String {
        return Build.MODEL
    }

    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

//    fun getFirebaseId(): String {
//        return FirebaseInstanceId.getInstance().id
//    }

    fun getVersionCode(context: Context): String {
        val packageInfo = getPackageInfo(context)

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            packageInfo.versionCode.toString()
        } else {
            packageInfo.longVersionCode.toString()
        }
    }

    fun getVersionName(context: Context): String {
        val packageInfo = getPackageInfo(context)
        return packageInfo.versionName
    }

    private fun getPackageInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    @SuppressLint("HardwareIds")
    fun getPhoneNumber(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ""
            }
            return manager.line1Number.replace("+82", "0")

        } catch(e: Exception)  {
            return ""
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceUUID(context: Context): String {
        val id = Preference.deviceUUID

        if (id.isNotEmpty()) {
            return UUID.fromString(id).toString()
        }

        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val uuid = try {
            if (androidId == "9774d56d682e549c") {
                UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {   //권한이 없을 경우 NULL로 넘김
                    return ""
                }

                val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))) else UUID.randomUUID() //디바이스 아이디도 못 가져올 경우 랜덤UUID 생성
            }
        } catch(e: Exception) {
            e.printStackTrace()
            UUID.randomUUID()
        }

        Preference.deviceUUID = uuid.toString()
        return uuid.toString()
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk")
    }
}