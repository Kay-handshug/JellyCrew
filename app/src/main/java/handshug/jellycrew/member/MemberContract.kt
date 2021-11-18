package handshug.jellycrew.member

interface MemberContract {

    companion object {
        const val ACTIVITY_CLOSE = 1000
        const val ACTIVITY_MAIN = 1010
        const val ACTIVITY_JOIN_TERMS = 1021
        const val ACTIVITY_JOIN_PHONE = 1022
        const val ACTIVITY_JOIN_EMAIL = 1023
        const val ACTIVITY_JOIN_PASSWORD = 1024
        const val ACTIVITY_JOIN_USER_INFO = 1025

        const val ACTIVITY_FIND_EMAIL = 1030
        const val ACTIVITY_FIND_PASSWORD = 1040
        const val ACTIVITY_PAST_ORDERS = 1050

        const val SHOW_DIALOG_USER_INFO_NOTI = 1100
        const val SHOW_DIALOG_TOAST_VERIFY_SEND = 1101
        const val SHOW_DIALOG_TOAST_VERIFY_FAIL = 1102
        const val SHOW_DIALOG_DATE_PICKER = 1103
        const val SHOW_DIALOG_GENDER = 1104

        const val COUNT_DOWN_TIMER_START = 1200
        const val COUNT_DOWN_TIMER_STOP = 1201

        const val START_LOGIN_KAKAO = 2000
        const val START_LOGIN_NAVER = 2010
        const val START_LOGIN_FACEBOOK = 2020

        const val MEMBER_LOGIN = 3000
        const val LOGIN_SUCCESS = 3010
    }
}