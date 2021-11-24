package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberPhoneVerifyData(
    @SerializedName("id") val id: Long,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("number") val number: String,
    @SerializedName("createdAt") val createdAt: String
)
