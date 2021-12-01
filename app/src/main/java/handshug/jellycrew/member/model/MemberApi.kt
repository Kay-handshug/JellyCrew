package handshug.jellycrew.member.model

import handshug.jellycrew.api.JellyCrewAPIPostResponse
import handshug.jellycrew.api.member.MemberJoinSuccessResponse
import handshug.jellycrew.api.member.MemberLoginResponse
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
        @Query("accountReferType") accountReferType: String,
        @Query("nickname") nickname: String,
        @Query("recommendFriend") recommendFriend: String,
        @Query("marketingAgreement") marketingAgreement: Boolean = true,
        @Query("lifeTimeMember") lifeTimeMember: Boolean = true,
        @Query("socialType") socialType: String,
        @Query("socialId") socialId: String,
        @Query("socialEmail") socialEmail: String,
        @Query("accessToken") accessToken: String,
        @Query("refreshToken") refreshToken: String
    ): JellyCrewAPIPostResponse<MemberJoinSuccessResponse>

    // 이메일 로그인
    @POST("/api/account/v1/login")
    suspend fun loginEmail(
            @Body params: MutableMap<String, Any>
    ): JellyCrewAPIPostResponse<MemberLoginResponse>

    // 소셜 로그인
    @POST("/api/account/v1/login/social")
    suspend fun loginSocial(
            @Body  params: MutableMap<String, Any>
    ): JellyCrewAPIPostResponse<MemberLoginResponse>
}