package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberTokenData(
    @SerializedName("additionalInformation") val additionalInformation: Long,
    @SerializedName("expiration") val expiration: String,
    @SerializedName("expired") val expired: String,
    @SerializedName("expiresIn") val expiresIn: String,
    @SerializedName("refreshToken") val refreshToken: MemberRefereshTokenData,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("value") val value: String,
    @SerializedName("scope") val scope: ArrayList<String>
)
