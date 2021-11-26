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

    // 0 : jellyCrew, 1: kakao, 2: naver, 3: facebook
    var loginType: Int
        get() = sharedPreference.getInt("loginType", 0)
        set(loginType) {
            sharedPreference.edit().putInt("loginType", loginType).apply()
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

    var accessToken: String
        get() = sharedPreference.getString("accessToken", "").toString()
        set(accessToken) {
            sharedPreference.edit().putString("accessToken", accessToken).apply()
        }

    var refreshToken: String
        get() = sharedPreference.getString("refreshToken", "").toString()
        set(refreshToken) {
            sharedPreference.edit().putString("refreshToken", refreshToken).apply()
        }

    var accessTokenExpiredAt: Long
        get() = sharedPreference.getLong("accessTokenExpiredAt", -1L)
        set(accessTokenExpiredAt) {
            sharedPreference.edit().putLong("accessTokenExpiredAt", accessTokenExpiredAt).apply()
        }

    var refreshTokenExpiredAt: Long
        get() = sharedPreference.getLong("refreshTokenExpiredAt", -1L)
        set(refreshTokenExpiredAt) {
            sharedPreference.edit().putLong("refreshTokenExpiredAt", refreshTokenExpiredAt).apply()
        }


    /**
     * User Info ############################# Start
     */

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
        get() = sharedPreference.getString("userName", "").toString()
        set(userName) {
            sharedPreference.edit().putString("userName", userName).apply()
        }

    var userBirth: String
        get() = sharedPreference.getString("userBirth", "").toString()
        set(userBirth) {
            sharedPreference.edit().putString("userBirth", userBirth).apply()
        }

    // W: female, M: male, N:3rd(제3의 성)
    var userGender: String
        get() = sharedPreference.getString("userGender", "W").toString()
        set(userGender) {
            sharedPreference.edit().putString("userGender", userGender).apply()
        }

    var userNickname: String
        get() = sharedPreference.getString("userNickname", "").toString()
        set(userNickname) {
            sharedPreference.edit().putString("userNickname", userNickname).apply()
        }

    var userFriend: String
        get() = sharedPreference.getString("userFriend", "").toString()
        set(userFriend) {
            sharedPreference.edit().putString("userFriend", userFriend).apply()
        }

    var isMarketingAgree: Boolean
        get() = sharedPreference.getBoolean("isMarketingAgree", true)
        set(isMarketingAgree) {
            sharedPreference.edit().putBoolean("isMarketingAgree", isMarketingAgree).apply()
        }

    var isLifeTimeMember: Boolean
        get() = sharedPreference.getBoolean("isLifeTimeMember", true)
        set(isLifeTimeMember) {
            sharedPreference.edit().putBoolean("isLifeTimeMember", isLifeTimeMember).apply()
        }

    var userSocialType: String
        get() = sharedPreference.getString("userSocialType", "").toString()
        set(userSocialType) {
            sharedPreference.edit().putString("userSocialType", userSocialType).apply()
        }

    var userSocialId: String
        get() = sharedPreference.getString("userSocialId", "").toString()
        set(userSocialId) {
            sharedPreference.edit().putString("userSocialId", userSocialId).apply()
        }

    var userSocialEmail: String
        get() = sharedPreference.getString("userSocialEmail", "").toString()
        set(userSocialEmail) {
            sharedPreference.edit().putString("userSocialEmail", userSocialEmail).apply()
        }

    /**
     * User Info ############################# End
     */
}
