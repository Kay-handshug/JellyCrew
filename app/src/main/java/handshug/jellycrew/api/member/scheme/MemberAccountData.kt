package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberAccountData(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("name") val name: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("genderType") val genderType: String,
    @SerializedName("recommendFriend") val recommendFriend: String,
    @SerializedName("accountReferType") val accountReferType: String,
    @SerializedName("agreeMarketingAt") val agreeMarketingAt: String,
    @SerializedName("disAgreeMarketingAt") val disAgreeMarketingAt: String,
    @SerializedName("joinedAt") val joinedAt: String,
    @SerializedName("joinedId") val joinedId: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("deletedAt") val deletedAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("lifeTimeMember") val lifeTimeMember: Boolean
)
