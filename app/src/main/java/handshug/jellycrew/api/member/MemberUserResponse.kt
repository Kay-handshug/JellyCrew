package handshug.jellycrew.api.member

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.api.member.scheme.MemberUserData

data class MemberUserResponse(
    @SerializedName("resultCode") val resultCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("totalCount") val totalCount: Int?,
    @SerializedName("data") val data: MemberUserData
)
