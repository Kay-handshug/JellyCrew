package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberUserData(
    @SerializedName("userId") val userId: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("scopes") val scopes: ArrayList<String>
)
