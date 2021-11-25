package handshug.jellycrew.member.model

import handshug.jellycrew.api.JellyCrewAPIPostResponse
import handshug.jellycrew.api.member.MemberJoinSuccessResponse
import handshug.jellycrew.api.member.MemberLoginEmailResponse
import handshug.jellycrew.api.member.MemberPhoneCheckMigrationResponse
import handshug.jellycrew.api.member.scheme.MemberPhoneVerifyData
import retrofit2.http.Body
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
    @POST("/api/account/v1/create")
    suspend fun joinConfirm(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("mobile") mobile: String,
        @Query("birth") birth: String,
        @Query("genderType") genderType: String,
        @Query("name") name: String,
        @Query("accountReferType") accountReferType: String
    ): JellyCrewAPIPostResponse<MemberJoinSuccessResponse>

    @POST("/api/account/v1/login")
    suspend fun loginEmail(
            @Body params: MutableMap<String, Any>
    ): JellyCrewAPIPostResponse<MemberLoginEmailResponse>

    // Social Login ///// http://10.3.63.70:5000
    @POST("/api/swagger-ui.html#/account-controller/loginSocialAccountUsingPOST")
    suspend fun loginSocial(
            @Body  params: MutableMap<String, Any>
    ): JellyCrewAPIPostResponse<String>
}