package handshug.jellycrew.utils

object ResponseCode {
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404

        const val INTERNAL_SERVER_ERROR = "500"

        const val SUCCESS = "0001"      // 200
        const val FAILURE = "2000"      // 400~

        const val ERROR_CODE_1000 = "1000" // 서버 내부 오류입니다.
        const val ERROR_CODE_2000 = "2000" // 권한이 없습니다.
        const val ERROR_CODE_2001 = "2001" // 해당 유저를 찾을 수 없습니다.
        const val ERROR_CODE_2004 = "2004" // 유효하지 않은 토큰입니다.
        const val ERROR_CODE_2006 = "2006" // 비밀번호가 일치하지 않습니다.
        const val ERROR_CODE_2100 = "2100" // 등록된 게스트 계정 PhoneUid 와 일치하지 않습니다. 새로운 게스트 계정을 생성해주세요.


        const val ERROR_CODE_2212 = "2212" // 지원하지 않는 계정 어카운트 타입입니다.

        const val ERROR_CODE_7002 = "7002" // 올바르지 않은 App client 입니다.
}