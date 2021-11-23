package handshug.jellycrew.member.model

import com.google.gson.annotations.SerializedName

data class MemberPhoneVerifySendResponse(
    @SerializedName("code") val resultCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: String
)
