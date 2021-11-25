package handshug.jellycrew.api.member

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.api.member.scheme.MemberTokenData

data class MemberLoginEmailResponse(
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("token") val token: MemberTokenData
)
