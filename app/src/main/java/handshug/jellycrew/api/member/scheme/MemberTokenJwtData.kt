package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberTokenJwtData(
    @SerializedName("accountId") val accountId: Long,
    @SerializedName("client_id") val client_id: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("exp") val exp: String
)
