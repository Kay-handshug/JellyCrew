package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberAccountSocialsData(
    @SerializedName("id") val id: Long,
    @SerializedName("accountId") val accountId: Long,
    @SerializedName("email") val email: String,
    @SerializedName("socialId") val socialId: String,
    @SerializedName("socialType") val socialType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
