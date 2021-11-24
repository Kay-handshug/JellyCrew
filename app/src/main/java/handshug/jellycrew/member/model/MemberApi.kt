package handshug.jellycrew.member.model

import handshug.jellycrew.api.JellyCrewAPIPostResponse
import handshug.jellycrew.api.member.MemberPhoneCheckMigrationResponse
import handshug.jellycrew.api.member.scheme.MemberPhoneVerifyData
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberApi {

    // 폰인증 시작 요청
    @POST("/api/phone/v1/start")
    suspend fun phoneVerifySend(
            @Query("mobile") mobile: String
    ): JellyCrewAPIPostResponse<String>

    // 폰인증 요청
    @POST("/api/phone/v1/verify")
    suspend fun phoneVerifyConfirm(
        @Query("mobile") mobile: String,
        @Query("number") number: String
    ): JellyCrewAPIPostResponse<MemberPhoneVerifyData>

    // 기존 이메일 사용 여부 확인
    @POST("/api/account/v1/check-email")
    suspend fun checkEmail(
            @Query("email") email: String
    ): JellyCrewAPIPostResponse<Boolean>

    // 기존 회원 여부 확인
    @POST("/api/account/v1/check-migration")
    suspend fun checkMigration(
        @Query("mobile") mobile: String,
        @Query("name") name: String
    ): JellyCrewAPIPostResponse<MemberPhoneCheckMigrationResponse>

    // 회원가입

}