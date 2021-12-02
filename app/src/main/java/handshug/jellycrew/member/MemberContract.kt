package handshug.jellycrew.member

interface MemberContract {

    companion object {
        const val ACTIVITY_CLOSE = 1000
        const val ACTIVITY_MAIN = 1010
        const val ACTIVITY_LOGIN = 1014
        const val ACTIVITY_LOGIN_HOME = 1015
        const val ACTIVITY_LOGIN_EMAIL = 1016
        const val ACTIVITY_JOIN_CONFIRM = 1020
        const val ACTIVITY_JOIN_SUCCESS = 1021

        const val ACTIVITY_FIND_EMAIL = 1030
        const val ACTIVITY_RESET_PASSWORD = 1040
        const val ACTIVITY_PAST_ORDERS = 1050

        const val FRAGMENT_JOIN_TERMS = 1201
        const val FRAGMENT_JOIN_PHONE = 1202
        const val FRAGMENT_JOIN_EMAIL = 1203
        const val FRAGMENT_JOIN_EMAIL_CAFE24 = 1204
        const val FRAGMENT_JOIN_PASSWORD = 1205
        const val FRAGMENT_JOIN_USER_INFO = 1206

        const val REQ_PHONE_VERIFY_CONFIRM = 1250

        const val SHOW_DIALOG_FINISH = 1300
        const val SHOW_DIALOG_USER_INFO_NOTI = 1301
        const val SHOW_DIALOG_TOAST_VERIFY_SEND = 1302
        const val SHOW_DIALOG_DATE_PICKER = 1304
        const val SHOW_DIALOG_GENDER = 1305
        const val SHOW_DIALOG_TERMS_DETAIL_01 = 1306
        const val SHOW_DIALOG_TERMS_DETAIL_02 = 1307
        const val SHOW_DIALOG_REQUEST_VERIFY_SEND_FAIL = 1308

        const val COUNT_DOWN_TIMER_START = 1504
        const val COUNT_DOWN_TIMER_STOP = 1505

        const val START_LOGIN_KAKAO = 2000
        const val START_LOGIN_NAVER = 2010
        const val START_LOGIN_FACEBOOK = 2020

        const val MEMBER_LOGIN = 3000
        const val LOGIN_SUCCESS = 3010
        const val LOGIN_FAIL = 3015

        const val KAKAO = "KAKAO"
        const val NAVER = "NAVER"
        const val FACEBOOK = "FACEBOOK"
    }
}