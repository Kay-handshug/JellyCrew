package handshug.jellycrew.member.model

import retrofit2.http.POST
import retrofit2.http.Query

interface MemberApi {

    // 폰인증 시작 요청
    @POST("/api/phone/v1/start")
    suspend fun phoneVerifySend(
            @Query("mobile") mobile: String
    ): MemberPhoneVerifySendResponse

    // 폰인증 요청
    @POST("/api/phone/v1/verify")
    suspend fun phoneVerifyConfirm(
        @Query("mobile") mobile: String,
        @Query("number") number: String
    ): MemberPhoneVerifyConfirmResponse
}