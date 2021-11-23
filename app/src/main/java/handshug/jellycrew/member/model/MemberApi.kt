package handshug.jellycrew.member.model

import retrofit2.http.POST
import retrofit2.http.Query

interface MemberApi {

    // 폰인증 시작 요청
    @POST("/api/phone/v1/start")
    suspend fun phoneVerifyStart(
            @Query("mobile") mobile: String
    ): MemberPhoneVerifyResponse

}