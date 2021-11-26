package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberTokenData(
    @SerializedName("accountId") val accountId: Long,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("expires_in") val expires_in: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("jti") val jti: String
)
