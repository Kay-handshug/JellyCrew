package handshug.jellycrew.utils

object ResponseCode {
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404

        const val SUCCESS = "0001"      // 200
        const val FAILURE = "2000"      // 400~

        const val INTERNAL_SERVER_ERROR = 500

        const val SPIDOR_API_FAIL_CODE = 0
        const val SPIDOR_API_SUCCESS_CODE = 1
}