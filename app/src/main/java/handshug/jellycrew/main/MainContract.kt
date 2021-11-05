package handshug.jellycrew.main

interface MainContract {

    companion object {
        const val ACTIVITY_SPLASH = 1000
        const val ACTIVITY_MAIN = 1001
        const val DRAWER_MENU_OPEN = 1002

        const val RESULT_REQ_MULTIPLE_PERMISSIONS = 2000
        const val RESULT_REQ_PERMISSIONS_GRANTED = 2010
        const val RESULT_REQ_PERMISSIONS_DENIED = 2020

        const val APP_END_DELAY_TIME = 2000L
    }
}