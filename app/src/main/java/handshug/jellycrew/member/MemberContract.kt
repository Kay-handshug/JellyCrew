package handshug.jellycrew.member

interface MemberContract {

    companion object {
        const val ACTIVITY_CLOSE = 1000
        const val ACTIVITY_MAIN = 1010
        const val ACTIVITY_JOIN_EMAIL = 1020
        const val ACTIVITY_FIND_EMAIL = 1030
        const val ACTIVITY_FIND_PASSWORD = 1040

        const val START_LOGIN_KAKAO = 2000
        const val START_LOGIN_NAVER = 2010

        const val MEMBER_LOGIN = 3000
        const val LOGIN_SUCCESS = 3010
    }

    fun navigateToJoinEmail()

    fun navigateToFindEmail()

    fun navigateToFindPassword()

    fun memberLogin()

    fun verifyPhoneNumberTemplate(phoneNumber: String): Boolean

    fun verifyMailTemplate(mail: String): Boolean

    fun verifyPasswordTemplate(password: String): Boolean

}