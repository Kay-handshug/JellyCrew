package handshug.jellycrew.home

interface HomeContract {
    companion object {
        const val START_PAYMENT_01 = 1000   // PG 결제
        const val START_PAYMENT_02 = 1001   // 간편결제
        const val START_PAYMENT_03 = 1002   // 통합결제
        const val START_PAYMENT_04 = 1003   // 간편결제 (비밀번호)
        const val START_PAYMENT_05 = 1004   // 간편결제 (생체인증)

        const val OPEN_DRAWER = 1050
    }
}