package handshug.jellycrew

import android.content.Context
import org.koin.core.context.GlobalContext

object Preference {
    val context: Context = GlobalContext.get().koin.get()
    private val sharedPreference = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = sharedPreference.getBoolean("isLogin", false)
        set(flag) {
            sharedPreference.edit().putBoolean("isLogin", flag).apply()
        }

    var loginKey: String
        get() = sharedPreference.getString("loginKey", "").toString()
        set(loginKey) {
            sharedPreference.edit().putString("loginKey", loginKey).apply()
        }

    var deviceUUID: String
        get() = sharedPreference.getString("deviceUUID", "").toString()
        set(uuid) {
            sharedPreference.edit().putString("deviceUUID", uuid).apply()
        }

    var userPhoneNumber: String
        get() = sharedPreference.getString("userPhoneNumber", "").toString()
        set(userPhoneNumber) {
            sharedPreference.edit().putString("userPhoneNumber", userPhoneNumber).apply()
        }

    var userEmail: String
        get() = sharedPreference.getString("userEmail", "").toString()
        set(userName) {
            sharedPreference.edit().putString("userEmail", userName).apply()
        }

    var userPassword: String
        get() = sharedPreference.getString("userPassword", "").toString()
        set(userPassword) {
            sharedPreference.edit().putString("userPassword", userPassword).apply()
        }

    var userId: Long
        get() = sharedPreference.getLong("userId", -1L)
        set(userId) {
            sharedPreference.edit().putLong("userId", userId).apply()
        }

    var userName: String
        get() = sharedPreference.getString("userName", "영웅").toString()
        set(userName) {
            sharedPreference.edit().putString("userName", userName).apply()
        }
}
