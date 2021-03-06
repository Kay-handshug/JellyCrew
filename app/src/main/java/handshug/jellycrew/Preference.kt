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

    var isCafe24MemberJoin: Boolean
        get() = sharedPreference.getBoolean("isCafe24MemberJoin", false)
        set(isCafe24MemberJoin) {
            sharedPreference.edit().putBoolean("isCafe24MemberJoin", isCafe24MemberJoin).apply()
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

    var socialAccessToken: String
        get() = sharedPreference.getString("socialAccessToken", "").toString()
        set(socialAccessToken) {
            sharedPreference.edit().putString("socialAccessToken", socialAccessToken).apply()
        }

    var socialRefreshToken: String
        get() = sharedPreference.getString("socialRefreshToken", "").toString()
        set(socialRefreshToken) {
            sharedPreference.edit().putString("socialRefreshToken", socialRefreshToken).apply()
        }

    var socialAccessTokenExpiredAt: Long
        get() = sharedPreference.getLong("socialAccessTokenExpiredAt", -1L)
        set(socialAccessTokenExpiredAt) {
            sharedPreference.edit().putLong("socialAccessTokenExpiredAt", socialAccessTokenExpiredAt).apply()
        }

    var socialRefreshTokenExpiredAt: Long
        get() = sharedPreference.getLong("socialRefreshTokenExpiredAt", -1L)
        set(socialRefreshTokenExpiredAt) {
            sharedPreference.edit().putLong("socialRefreshTokenExpiredAt", socialRefreshTokenExpiredAt).apply()
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

    // W: female, M: male, N:3rd(???3??? ???)
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

    var userMoney: Long
        get() = sharedPreference.getLong("userMoney", 0L)
        set(userMoney) {
            sharedPreference.edit().putLong("userMoney", userMoney).apply()
        }

    var isMarketingAgree: Boolean
        get() = sharedPreference.getBoolean("isMarketingAgree", false)
        set(isMarketingAgree) {
            sharedPreference.edit().putBoolean("isMarketingAgree", isMarketingAgree).apply()
        }

    var isLifeTimeMember: Boolean
        get() = sharedPreference.getBoolean("isLifeTimeMember", false)
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

    var userSocialName: String
        get() = sharedPreference.getString("userSocialName", "").toString()
        set(userSocialName) {
            sharedPreference.edit().putString("userSocialName", userSocialName).apply()
        }

    var userSocialEmail: String
        get() = sharedPreference.getString("userSocialEmail", "").toString()
        set(userSocialEmail) {
            sharedPreference.edit().putString("userSocialEmail", userSocialEmail).apply()
        }

    var userSocialPhoneNumber: String
        get() = sharedPreference.getString("userSocialPhoneNumber", "").toString()
        set(userSocialPhoneNumber) {
            sharedPreference.edit().putString("userSocialPhoneNumber", userSocialPhoneNumber).apply()
        }

    var userSocialNickname: String
        get() = sharedPreference.getString("userSocialNickname", "").toString()
        set(userSocialNickname) {
            sharedPreference.edit().putString("userSocialNickname", userSocialNickname).apply()
        }

    var userSocialPhoto: String
        get() = sharedPreference.getString("userSocialPhoto", "").toString()
        set(userSocialPhoto) {
            sharedPreference.edit().putString("userSocialPhoto", userSocialPhoto).apply()
        }

    var userSocialGender: String
        get() = sharedPreference.getString("userSocialGender", "").toString()
        set(userSocialGender) {
            sharedPreference.edit().putString("userSocialGender", userSocialGender).apply()
        }

    var userSocialBirthDay: String
        get() = sharedPreference.getString("userSocialBirthDay", "").toString()
        set(userSocialBirthDay) {
            sharedPreference.edit().putString("userSocialBirthDay", userSocialBirthDay).apply()
        }

    /**
     * User Info ############################# End
     */
}
