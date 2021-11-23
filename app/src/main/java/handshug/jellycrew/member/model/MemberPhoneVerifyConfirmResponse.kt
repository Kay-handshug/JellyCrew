package handshug.jellycrew.member.model

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.member.model.scheme.MemberPhoneVerifyData

data class MemberPhoneVerifyConfirmResponse(
    @SerializedName("code") val resultCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MemberPhoneVerifyData
)
